package fr.jervill.springbatch;

import fr.jervill.springbatch.batchutils.BatchJobListener;
import fr.jervill.springbatch.batchutils.BatchStepSkipper;
import fr.jervill.springbatch.dto.ConvertedInputData;
import fr.jervill.springbatch.dto.InputData;
import fr.jervill.springbatch.processor.BatchProcessor;
import fr.jervill.springbatch.reader.BatchReader;
import fr.jervill.springbatch.writer.BatchWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@SpringBootApplication //annotation Spring Boot qui encapsule les trois annotations suivantes : @Configuration, @EnableAutoConfiguration, @ComponentScan
@EnableBatchProcessing //annotation qui déclare une classe qui va permettre de créer et configurer des composants Spring Batch.
public class SpringbatchformationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbatchformationApplication.class, args);
	}

	@Value("${working.directory}")
	private String workDirPath;

	@Autowired
	private DataSource dataSource;

	@Autowired
	PlatformTransactionManager transactionManager;

	@Bean //(5) correspond au composant Repository de l'architecture Spring Batch numéroté 5 sur la figure de la section II.
	public JobRepository jobRepositoryObj() throws Exception{
		JobRepositoryFactoryBean jobRepoFactory = new JobRepositoryFactoryBean();
		jobRepoFactory.setTransactionManager(transactionManager);
		jobRepoFactory.setDataSource(dataSource);
		return jobRepoFactory.getObject();
	}

	@Autowired
	//classe native Spring Batch qui offre des fonctionnalités permettant de créer et d'enrichir un Job de tous les éléments dont il a besoin.
	JobBuilderFactory jobBuilderFactory;

	@Autowired
	//classe native Spring Batch qui offre des fonctionnalités aidant à créer et enrichir une Step de tous les éléments dont elle a besoin.
	StepBuilderFactory stepBuilderFactory;

	@Bean
	//(4) classe qui effectuera la lecture des données dans les fichiers csv entrés.
	public BatchReader batchReader(){
		return new BatchReader(workDirPath);
	}
	@Bean
	//(4) classe qui effectuera les traitements nécessaires sur les données lues.
	public BatchProcessor batchProcessor(){
		return new BatchProcessor();
	}
	@Bean
	//(4) classe qui va sauvegarder les données traitées dans l'entrepôt de données.
	public BatchWriter batchWriter(){
		return new BatchWriter();
	}

	@Bean
	//classe qui permettra de journaliser les évènements pendant l'exécution du Job.
	public BatchJobListener batchJobListener(){
		return new BatchJobListener();
	}

	@Bean
	/*classe qui va permettre la gestion de la tolérance aux fautes. En d'autres termes, cette classe va permettre au
	Batch de tolérer certaines lignes non conformes lues par le Reader en les sautant tout simplement, pour ne pas
	pénaliser toutes les autres lignes qui pourraient être conformes. En général un seuil est paramétré et c'est à
	l'atteinte de ce seuil que la tolérance aux fautes n'est plus acceptée et le Batch s'arrêtera donc.*/
	public BatchStepSkipper batchStepSkipper(){
		return new BatchStepSkipper();
	}

	@Bean
	/*(3) classe native Spring Batch à laquelle on enrichira le Reader, le Processor et le Writer du Batch. De plus, on va
	lui indiquer le Pojo dans lequel seront enregistrées les données lues par le Reader depuis les fichiers d'entrées
	(ici, InputData) et sous quel format le Processor après les avoir transformées les délivrera au Writer
	(ici, ConvertedInputData). L'argument chunk définit le nombre de données lues et traitées à la fois par le Reader.
	À savoir qu'un Reader Spring Batch partitionne les données d'entrées et les lit par lot (défini donc par le chunk).
	Enfin, nous valorisons le champ skipPolicy avec le bean qui va se charger de gérer la tolérance aux fautes
	(en l'occurrence, BatchStepSkipper).
	 */
	public Step batchStep(){
		return stepBuilderFactory.get("stepDatawarehouseLoader").transactionManager(transactionManager)
				.<InputData, ConvertedInputData>chunk(1).reader(batchReader()).processor(batchProcessor())
				.writer(batchWriter()).faultTolerant().skipPolicy(batchStepSkipper()).build();
	}

	@Bean
	//(2) classe native Spring Batch dans laquelle on enrichira notre Step précédente, le listener et le jobRepository.
	public Job jobStep() throws Exception{
		return jobBuilderFactory.get("jobDatawarehouseLoader").repository(jobRepositoryObj()).incrementer(new RunIdIncrementer()).listener(batchJobListener())
				.flow(batchStep()).end().build();
	}
}

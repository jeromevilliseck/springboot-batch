package fr.jervill.springbatch.reader;

import fr.jervill.springbatch.dto.InputData;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.core.io.FileSystemResource;
import org.springframework.validation.BindException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BatchReader extends MultiResourceItemReader<InputData> {
    Logger logger = LoggerFactory.getLogger(BatchReader.class);

    public BatchReader(String workDirPath){
        //To know which folder is processed
        File inputDir = new File(workDirPath);
        logger.info("Path to process : " + inputDir.getAbsolutePath());

        List<String> result = new ArrayList<>();
        this.search(".*\\.csv", inputDir, result);
        logger.info("Files to process : ");
        for (String s : result) {
            logger.info(s.substring(s.lastIndexOf("/") + 1)); //UNIX
            //logger.info(s.substring(s.lastIndexOf("\\")+1)); //WINDOWS
        }

        logger.info("Batch reader starting to read input data in repository : " + workDirPath);
        this.setResources(getInputResources(workDirPath));
        this.setDelegate(readOneFile());
    }

    /**
     * Get all the resources (files) to be readen by this spring batch reader.
     *
     * @param workDirPath
     * @return
     */
    private FileSystemResource[] getInputResources(String workDirPath){
        File inputDir = new File(workDirPath);
        File[] files = inputDir.listFiles();
        List<File> filesList = Arrays.asList(files);

        List<FileSystemResource> inputResources = filesList.stream().filter(
                file -> file != null && file.isFile())
                .peek(file -> logger.info("Reading file : " + file.getAbsolutePath()))
                .map(file -> new FileSystemResource(file))
                .collect(Collectors.toList());

        return inputResources.toArray(new FileSystemResource[inputResources.size()]);
    }

    /**
     * Set and return a FlatFileItemReader to read one resource.
     *
     * @return
     */
    private FlatFileItemReader<InputData> readOneFile() {

        FlatFileItemReader<InputData> resourceReader = new FlatFileItemReader<InputData>();

        //skip the first line which is the file header
        resourceReader.setLinesToSkip(1);

        resourceReader.setLineMapper(new DefaultLineMapper<InputData>() {

            private DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer(";");
            private FieldSetMapper<InputData> fieldSetMapper = new BeanWrapperFieldSetMapper<InputData>() {
                @Override
                public InputData mapFieldSet(FieldSet pFielSet) throws BindException {
                    InputData inputData = new InputData();
                    inputData.setProductName(pFielSet.readString("productName"));
                    inputData.setProductEanCode(pFielSet.readString("productEanCode"));
                    inputData.setProductType(pFielSet.readString("productType"));
                    inputData.setProductAmount(pFielSet.readDouble("productAmount"));
                    inputData.setProductQuantity(pFielSet.readInt("productQuantity"));
                    inputData.setSupplierName(pFielSet.readString("supplierName"));
                    inputData.setSupplierAddress(pFielSet.readString("supplierAddress"));
                    inputData.setPurchaserEmail(pFielSet.readString("purchaserEmail"));
                    inputData.setPurchaserFirstName(pFielSet.readString("purchaserFirstName"));
                    inputData.setPurchaserLastName(pFielSet.readString("purchaserLastName"));
                    inputData.setTransactionDate(pFielSet.readDate("transactionDate"));
                    return inputData;

                }
            };

            @Override
            public InputData mapLine(String pLine, int pLineNumber) throws Exception {

                lineTokenizer.setNames(new String[]{"productName", "productEanCode", "productType", "productQuantity",
                        "productAmount", "supplierName", "supplierAddress", "purchaserFirstName",
                        "purchaserLastName", "purchaserEmail", "transactionDate"});
                return fieldSetMapper.mapFieldSet(lineTokenizer.tokenize(pLine));
            }

        });

        return resourceReader;
    }

    private void search(final String pattern, final File folder, List<String> result) {
        for (final File f : folder.listFiles()) {

            if (f.isDirectory()) {
                search(pattern, f, result);
            }

            if (f.isFile()) {
                if (f.getName().matches(pattern)) {
                    result.add(f.getAbsolutePath());
                }
            }

        }
    }
}

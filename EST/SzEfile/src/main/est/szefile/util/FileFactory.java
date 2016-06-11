package est.szefile.util;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import est.commons.dataparser.factory.Factory;
import est.commons.dataparser.filter.ResultFilter;
import est.commons.dataparser.parser.Parser;
import est.commons.dataparser.store.strategy.StoreStrategy;
import est.szefile.SzEFileParser;

@Component
public class FileFactory implements Factory {
    private static final Logger LOGGER = Logger.getLogger(FileFactory.class);

    public FileFactory() {
        LOGGER.info("create FileFactory...");
    }

    private SzEFileParser eFileParser = null;

    @Override
    public synchronized Parser getParser(File file) {
        String fileName = file.getName();
        if (fileName.endsWith(Config.Efile_SuffixName)) {
            if (eFileParser == null) {// EFileParser只有一个实例
                eFileParser = new SzEFileParser();
            }
            return eFileParser;
        }
        return null;
    }

    @Override
    public StoreStrategy getStoreStrategy(Parser parser) {
        return new StoreStrategy() {

            @Override
            public void store(Object bean) {
            }

            @Override
            public void store(List<Object> rstList) {
            }
        };
    }

    @Override
    public List<ResultFilter> getRstFilters(Parser parser) {
        return new LinkedList<ResultFilter>();
    }

}

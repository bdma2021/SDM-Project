package enodata.com;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import enodata.com.utils.Constants;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDFS;

import java.io.FileReader;
import java.util.List;

public class EnodataCatalog {
    private static EnodataCatalog enodataCatalog;

    private String directoryBasePath;

    private static final String DATA_SOURCE_FILE = "datasource.csv";
    private static final String FILE_FILE = "file.csv";
    private static final String ATTRIBUTE_FILE = "attribute.csv";
    private static final String RELATIONAL_DATABASE_FILE = "relationaldatabase.csv";
    private static final String TABLE_FILE = "table.csv";
    private static final String COLUMN_FILE = "column.csv";

    private EnodataCatalog() {}

    public static EnodataCatalog getInstance() {
        if(enodataCatalog == null) {
            enodataCatalog = new EnodataCatalog();
        }
        return enodataCatalog;
    }

    public void setDirectoryBasePath(String directoryBasePath) {
        this.directoryBasePath = directoryBasePath;
    }

    public void createInstances(ModelBuilder builder, String directoryBasePath) {
        setDirectoryBasePath(directoryBasePath);
        InsertDataSource(builder);
        InsertFile(builder);
        InsertAttribute(builder);
        InsertRelationalDatabase(builder);
        InsertTable(builder);
        InsertColumn(builder);
    }

    public void InsertDataSource(ModelBuilder builder) {
        List<String[]> instances = this.readCSV(fullPath(DATA_SOURCE_FILE));
        instances.forEach(line -> {
            IRI id = iri(line[0]);
            String baseUrl = line[1];
            String label = line[2];
            String comment = line[3];
            builder.subject(id).add(CatalogSchema.BASE_URL, baseUrl);
            addLabel(builder, id, label);
            addComment(builder, id, comment);
        });
    }

    public void InsertFile(ModelBuilder builder) {
        List<String[]> instances = this.readCSV(fullPath(FILE_FILE));
        instances.forEach(line -> {
            IRI id = iri(line[0]);
            String url = line[1];
            String format = line[2];
            String version = line[3];
            String timestamp = line[4];
            IRI datasource = iri(line[5]);
            String label = line[6];
            String comment = line[7];
            builder.subject(id)
                    .add(CatalogSchema.URL, url)
                    .add(CatalogSchema.FORMAT, format)
                    .add(CatalogSchema.VERSION, version)
                    .add(CatalogSchema.TIMESTAMP, timestamp)
                    .add(RDFS.MEMBER, datasource);
            addLabel(builder, id, label);
            addComment(builder, id, comment);
        });
    }

    public void InsertAttribute(ModelBuilder builder) {
        List<String[]> instances = this.readCSV(fullPath(ATTRIBUTE_FILE));
        instances.forEach(line -> {
            IRI id = iri(line[0]);
            IRI file = iri(line[1]);
            IRI relatedAttr = iri(line[2]);
            String label = line[3];
            String comment = line[4];
            builder.subject(id).add(RDFS.MEMBER, file);
            if (!relatedAttr.equals("")) {
                builder.subject(id).add(CatalogSchema.RELATES_TO, relatedAttr);
            }
            addLabel(builder, id, label);
            addComment(builder, id, comment);
        });
    }

    public void InsertRelationalDatabase(ModelBuilder builder) {
        List<String[]> instances = this.readCSV(fullPath(RELATIONAL_DATABASE_FILE));
        instances.forEach(line -> {
            IRI id = iri(line[0]);
            IRI datasource = iri(line[1]);
            String label = line[2];
            String comment = line[3];
            builder.subject(id).add(CatalogSchema.COMES_FROM_DS, datasource);
            addLabel(builder, id, label);
            addComment(builder, id, comment);
        });
    }

    public void InsertTable(ModelBuilder builder) {
        List<String[]> instances = this.readCSV(fullPath(TABLE_FILE));
        instances.forEach(line -> {
            IRI id = iri(line[0]);
            IRI database = iri(line[1]);
            String label = line[2];
            String comment = line[3];
            builder.subject(id).add(RDFS.MEMBER, database);
            addLabel(builder, id, label);
            addComment(builder, id, comment);
        });
    }

    public void InsertColumn(ModelBuilder builder) {
        List<String[]> instances = this.readCSV(fullPath(COLUMN_FILE));
        instances.forEach(line -> {
            IRI id = iri(line[0]);
            IRI table = iri(line[1]);
            String datatype = line[2];
            IRI originalAttr = iri(line[3]);
            IRI foreignKey = iri(line[4]);
            String label = line[5];
            String comment = line[6];
            builder.subject(id)
                    .add(RDFS.MEMBER, table)
                    .add(CatalogSchema.DATATYPE, datatype)
                    .add(CatalogSchema.COMES_FROM_ATTR, originalAttr);
            if (!foreignKey.equals("")) {
                builder.subject(id).add(CatalogSchema.FOREIGN_KEY, foreignKey);
            }
            addLabel(builder, id, label);
            addComment(builder, id, comment);
        });
    }

    private void addLabel(ModelBuilder builder, IRI subject, String label) {
        if (!label.equals("")) {
            builder.subject(subject).add(RDFS.LABEL, label);
        }
    }

    private void addComment(ModelBuilder builder, IRI subject, String comment) {
        if (!comment.equals("")) {
            builder.subject(subject).add(RDFS.COMMENT, comment);
        }
    }

    private List<String[]> readCSV(String fileName) {
        try (
                CSVReader reader = new CSVReaderBuilder(new FileReader(fileName))
                        .withSkipLines(1)
                        .build()
        ) {
            return reader.readAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private IRI iri(String resource) {
        return Values.iri(Constants.NAME_SPACE, resource);
    }

    private String fullPath(String filepath) {
        return this.directoryBasePath + filepath;
    }
}

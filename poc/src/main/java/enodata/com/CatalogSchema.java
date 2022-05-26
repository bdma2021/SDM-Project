package enodata.com;


import enodata.com.utils.Constants;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDFS;

public class CatalogSchema {
    private static CatalogSchema catalogSchema;
    private static final String NAME_SPACE = Constants.NAME_SPACE;

    // Classes
    static final IRI DATASOURCE = Values.iri(NAME_SPACE, "DataSource");
    static final IRI FILE = Values.iri(NAME_SPACE, "File");
    static final IRI ATTRIBUTE = Values.iri(NAME_SPACE, "Attribute");

    static final IRI DATABASE = Values.iri(NAME_SPACE, "Database");
    static final IRI RELATIONAL_DATABASE = Values.iri(NAME_SPACE, "RelationalDatabase");
    static final IRI TABLE = Values.iri(NAME_SPACE, "Table");
    static final IRI COLUMN = Values.iri(NAME_SPACE, "Column");

    // Properties
    static final IRI BASE_URL = Values.iri(NAME_SPACE, "baseUrl");
    static final IRI URL = Values.iri(NAME_SPACE, "url");
    static final IRI FORMAT = Values.iri(NAME_SPACE, "format");
    static final IRI VERSION = Values.iri(NAME_SPACE, "version");
    static final IRI TIMESTAMP = Values.iri(NAME_SPACE, "timestamp");
    static final IRI RELATES_TO = Values.iri(NAME_SPACE, "relatesTo");

    static final IRI COMES_FROM_DS = Values.iri(NAME_SPACE, "comesFromDs");
    static final IRI FOREIGN_KEY = Values.iri(NAME_SPACE, "foreignKey");
    static final IRI DATATYPE = Values.iri(NAME_SPACE, "datatype");
    static final IRI COMES_FROM_ATTR = Values.iri(NAME_SPACE, "comesFromAttr");

    private CatalogSchema() {}

    public static CatalogSchema getInstance() {
        if(catalogSchema == null) {
            catalogSchema = new CatalogSchema();
        }
        return catalogSchema;
    }

    public void createSchema(ModelBuilder builder) {
        // Subclasses
        builder.subject(CatalogSchema.RELATIONAL_DATABASE).add(RDFS.SUBCLASSOF, CatalogSchema.DATABASE);

        // Ranges and Domains
        builder.subject(CatalogSchema.BASE_URL).add(RDFS.DOMAIN, CatalogSchema.DATASOURCE);
        builder.subject(CatalogSchema.BASE_URL).add(RDFS.RANGE, RDFS.LITERAL);

        builder.subject(CatalogSchema.URL).add(RDFS.DOMAIN, CatalogSchema.FILE);
        builder.subject(CatalogSchema.URL).add(RDFS.RANGE, RDFS.LITERAL);
        builder.subject(CatalogSchema.FORMAT).add(RDFS.DOMAIN, CatalogSchema.FILE);
        builder.subject(CatalogSchema.FORMAT).add(RDFS.RANGE, RDFS.LITERAL);
        builder.subject(CatalogSchema.VERSION).add(RDFS.DOMAIN, CatalogSchema.FILE);
        builder.subject(CatalogSchema.VERSION).add(RDFS.RANGE, RDFS.LITERAL);
        builder.subject(CatalogSchema.TIMESTAMP).add(RDFS.DOMAIN, CatalogSchema.FILE);
        builder.subject(CatalogSchema.TIMESTAMP).add(RDFS.RANGE, RDFS.LITERAL);

        builder.subject(CatalogSchema.RELATES_TO).add(RDFS.DOMAIN, CatalogSchema.ATTRIBUTE);
        builder.subject(CatalogSchema.RELATES_TO).add(RDFS.RANGE, CatalogSchema.ATTRIBUTE);

        builder.subject(CatalogSchema.COMES_FROM_DS).add(RDFS.DOMAIN, CatalogSchema.DATABASE);
        builder.subject(CatalogSchema.COMES_FROM_DS).add(RDFS.RANGE, CatalogSchema.DATASOURCE);
        builder.subject(CatalogSchema.FOREIGN_KEY).add(RDFS.DOMAIN, CatalogSchema.COLUMN);
        builder.subject(CatalogSchema.FOREIGN_KEY).add(RDFS.RANGE, CatalogSchema.COLUMN);
        builder.subject(CatalogSchema.DATATYPE).add(RDFS.DOMAIN, CatalogSchema.COLUMN);
        builder.subject(CatalogSchema.DATATYPE).add(RDFS.RANGE, RDFS.DATATYPE);
        builder.subject(CatalogSchema.COMES_FROM_ATTR).add(RDFS.DOMAIN, CatalogSchema.COLUMN);
        builder.subject(CatalogSchema.COMES_FROM_ATTR).add(RDFS.RANGE, CatalogSchema.ATTRIBUTE);

        // Members
        builder.subject(CatalogSchema.FILE).add(RDFS.MEMBER, CatalogSchema.DATASOURCE);
        builder.subject(CatalogSchema.ATTRIBUTE).add(RDFS.MEMBER, CatalogSchema.FILE);

        builder.subject(CatalogSchema.TABLE).add(RDFS.MEMBER, CatalogSchema.RELATIONAL_DATABASE);
        builder.subject(CatalogSchema.COLUMN).add(RDFS.MEMBER, CatalogSchema.TABLE);

    }
}

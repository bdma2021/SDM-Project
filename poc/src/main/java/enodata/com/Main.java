package enodata.com;

import enodata.com.utils.Constants;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;

public class Main {

    // GraphDB
    private static final String GRAPHDB_SERVER = "http://localhost:7200/";
    private static final String REPOSITORY_ID = "enodata-catalog";

    private static final String DIRECTORY = "src/main/resources/data/";

    public static void main(String[] args) {
        System.out.println("Started creating models");
        Repository db = new HTTPRepository(GRAPHDB_SERVER, REPOSITORY_ID);

        ModelBuilder builder = new ModelBuilder().setNamespace("sdm", Constants.NAME_SPACE);

        ModelBuilder tboxBuilder = builder.namedGraph("enodata:schema");
        CatalogSchema catalogSchema = CatalogSchema.getInstance();
        catalogSchema.createSchema(tboxBuilder);

        EnodataCatalog enodataCatalog = EnodataCatalog.getInstance();
        enodataCatalog.createInstances(builder, DIRECTORY);

        Model model = builder.build();

        // Open a connection to the database
        try (RepositoryConnection conn = db.getConnection()) {
            // add the model
            conn.add(model);
            System.out.println("Models successfully added to GraphDB");
        } finally {
            // Close the connection
            db.shutDown();
        }
    }
}

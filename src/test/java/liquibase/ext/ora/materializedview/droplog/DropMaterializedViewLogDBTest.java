package liquibase.ext.ora.materializedview.droplog;

import liquibase.Contexts;
import liquibase.ext.ora.testing.BaseTestCase;

import org.dbunit.Assertion;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.junit.Before;
import org.junit.Test;

public class DropMaterializedViewLogDBTest extends BaseTestCase {

    private IDataSet loadedDataSet;
    private final String TABLE_NAME = "USER_MVIEW_LOGS";

    @Before
    public void setUp() throws Exception {
        changeLogFile = "liquibase/ext/ora/materializedview/droplog/changelog.test.xml";
        connectToDB();
        cleanDB();
    }

    protected IDatabaseConnection getConnection() throws Exception {
        return new DatabaseConnection(connection);
    }

    protected IDataSet getDataSet() throws Exception {
        loadedDataSet = new FlatXmlDataSet(this.getClass().getClassLoader().getResourceAsStream(
                "liquibase/ext/ora/materializedview/droplog/input.xml"));
        return loadedDataSet;
    }

    @Test
    public void testCompare() throws Exception {
        QueryDataSet actualDataSet = new QueryDataSet(getConnection());

        liquiBase.update(new Contexts());
        actualDataSet.addTable(TABLE_NAME, "SELECT MASTER, ROWIDS, PRIMARY_KEY from " + TABLE_NAME);
        loadedDataSet = getDataSet();
        actualDataSet.getTables(); // need to do this to force the SQL above to run before the rollback below

        Assertion.assertEquals(loadedDataSet, actualDataSet);
    }

}

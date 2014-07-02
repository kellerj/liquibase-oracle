package liquibase.ext.ora.grant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import liquibase.exception.ValidationErrors;
import liquibase.statement.AbstractSqlStatement;

public abstract class AbstractObjectPermissionStatement extends
		AbstractSqlStatement {

	protected String schemaName;
	protected String objectName;
	protected String recipientList;
	private Boolean select = Boolean.FALSE;
	private Boolean update = Boolean.FALSE;
	private Boolean insert = Boolean.FALSE;
	private Boolean delete = Boolean.FALSE;
	private Boolean execute = Boolean.FALSE;

	public AbstractObjectPermissionStatement() {
		super();
	}

    public AbstractObjectPermissionStatement(String schemaName, String objectName,
			String recipientList) {
		this();
		this.schemaName = schemaName;
		this.objectName = objectName;
		this.recipientList = recipientList;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getRecipientList() {
		return recipientList;
	}

	public void setRecipientList(String recipientList) {
		this.recipientList = recipientList;
	}

	public Boolean getSelect() {
		if ( select == null ) {
			return false;
		}
		return select;
	}

	public void setSelect(Boolean select) {
		this.select = select;
	}

	public Boolean getUpdate() {
		if ( update == null ) {
			return false;
		}
		return update;
	}

	public void setUpdate(Boolean update) {
		this.update = update;
	}

	public Boolean getInsert() {
		if ( insert == null ) {
			return false;
		}
		return insert;
	}

	public void setInsert(Boolean insert) {
		this.insert = insert;
	}

	public Boolean getDelete() {
		if ( delete == null ) {
			return false;
		}
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}

	public Boolean getExecute() {
		if ( execute == null ) {
			return false;
		}
		return execute;
	}

	public void setExecute(Boolean execute) {
		this.execute = execute;
	}

	public String getPermissionList() {
        List<String> permissions = new ArrayList<String>(5);
        if ( getSelect() ) {
        	permissions.add( "SELECT" );
        }
        if ( getUpdate() ) {
        	permissions.add( "UPDATE" );
        }
        if ( getInsert() ) {
        	permissions.add( "INSERT" );
        }
        if ( getDelete() ) {
        	permissions.add( "DELETE" );
        }
        if ( getExecute() ) {
        	permissions.add( "EXECUTE" );
        }
        return join(permissions.iterator(), ',');
	}

    /**
     * <p>Joins the elements of the provided <code>Iterator</code> into
     * a single String containing the provided elements.</p>
     *
     * <p>No delimiter is added before or after the list. Null objects or empty
     * strings within the iteration are represented by empty strings.</p>
     *
     * <p>See the examples here: {@link #join(Object[],char)}. </p>
     *
     * @param iterator  the <code>Iterator</code> of values to join together, may be null
     * @param separator  the separator character to use
     * @return the joined String, <code>null</code> if null iterator input
     * @since 2.0
     */
    protected String join(Iterator<String> iterator, char separator) {

        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return "";
        }
        String first = iterator.next();
        if (!iterator.hasNext()) {
            return first;
        }

        // two or more elements
        StringBuilder buf = new StringBuilder(256); // Java default is 16, probably too small
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            buf.append(separator);
            String obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }

        return buf.toString();
    }

	public ValidationErrors validate() {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.checkRequiredField("tableName", getObjectName());
        validationErrors.checkRequiredField("recipientList", getRecipientList());
        if ( !getSelect()
        		&& !getUpdate()
        		&& !getInsert()
        		&& !getDelete()
        		&& !getExecute()
        		) {
        	validationErrors.addError("You must specify at least one permission.");
        }
        return validationErrors;
    }

}
package liquibase.ext.ora.createSynonym;

import java.text.MessageFormat;

import liquibase.change.AbstractChange;
import liquibase.change.Change;
import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.ext.ora.dropSynonym.DropSynonymChange;
import liquibase.statement.SqlStatement;

@DatabaseChange(name = "createSynonym", description = "Create synonym", priority = ChangeMetaData.PRIORITY_DEFAULT)
public class CreateSynonymChange extends AbstractChange {

	private Boolean replace = Boolean.FALSE;
	private Boolean isPublic = Boolean.FALSE;
	private String objectName;
	private String objectSchemaName;

	private String synonymName;
	private String synonymSchemaName;

	public Boolean getReplace() {
		if ( replace == null ) {
			replace = Boolean.FALSE;
		}
		return replace;
	}

	public void setReplace(Boolean replace) {
		this.replace = replace;
	}

	public Boolean getPublic() {
		if ( isPublic == null ) {
			isPublic = Boolean.FALSE;
		}
		return isPublic;
	}

	public void setPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getObjectSchemaName() {
		return objectSchemaName;
	}

	public void setObjectSchemaName(String objectSchemaName) {
		this.objectSchemaName = objectSchemaName;
	}

	public String getSynonymName() {
		return synonymName;
	}

	public void setSynonymName(String synonymName) {
		this.synonymName = synonymName;
	}

	public String getSynonymSchemaName() {
		return synonymSchemaName;
	}

	public void setSynonymSchemaName(String synonymSchemaName) {
		this.synonymSchemaName = synonymSchemaName;
	}

    @Override
    public String getConfirmationMessage() {
        return MessageFormat.format("Synonym {0} created", getSynonymName());
    }

	@Override
	protected Change[] createInverses() {
		DropSynonymChange inverse = new DropSynonymChange();
		inverse.setPublic(getPublic());
		inverse.setSynonymName(getSynonymName());
		inverse.setSynonymSchemaName(getSynonymSchemaName());
		return new Change[] { inverse };
	}

	@Override
	public SqlStatement[] generateStatements(Database database) {
		CreateSynonymStatement statement = new CreateSynonymStatement();

		statement.setObjectName(getObjectName());
		statement.setObjectSchemaName(getObjectSchemaName());
		statement.setSynonymName(getSynonymName());
		statement.setSynonymSchemaName(getSynonymSchemaName());

		statement.setReplace(getReplace());
		statement.setPublic(getPublic());
		return new SqlStatement[] { statement };
	}

}

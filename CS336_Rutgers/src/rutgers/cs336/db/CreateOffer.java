package rutgers.cs336.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CreateOffer extends DBBase implements IDaoConstant {
	public static final String PREFIX_CATEGORY_NAME = "categoryName_";

	private static final String sqlInsertOffer = "insert Offer (offerId, categoryName, seller, min_price, startDate, endDate, status) VALUES (?, ?, ?, ?, NOW(), DATE_ADD(NOW(), INTERVAL + ? DAY), 1)";

	private static final String sqlInsertOfferField = "insert OfferField (offerId, fieldID, fieldText) VALUES (?, ?, ?)";

	public static Map doCreateOffer(Map<String, String[]> parameters) {
		Map output = new HashMap();
		//
		Connection        dbConnection            = null;
		PreparedStatement preparedStatementInsert = null;
		PreparedStatement preparedStatementUpdate = null;
		//
		try {
			dbConnection = getDBConnection();

			dbConnection.setAutoCommit(false);

			preparedStatementInsert = dbConnection.prepareStatement(insertTableSQL);
			preparedStatementInsert.setInt(1, 999);
			preparedStatementInsert.setString(2, "mkyong101");
			preparedStatementInsert.setString(3, "system");
			preparedStatementInsert.setTimestamp(4, getCurrentTimeStamp());
			preparedStatementInsert.executeUpdate();

			preparedStatementUpdate = dbConnection.prepareStatement(updateTableSQL);
			// preparedStatementUpdate.setString(1,
			// "A very very long string caused db error");
			preparedStatementUpdate.setString(1, "new string");
			preparedStatementUpdate.setInt(2, 999);
			preparedStatementUpdate.executeUpdate();

			dbConnection.commit();

			System.out.println("Done!");

		}
		catch (SQLException e) {
			dbConnection.rollback();
			e.printStackTrace();
			System.out.println(e.getMessage());

		}
		finally {

			if (preparedStatementInsert != null) {
				preparedStatementInsert.close();
			}

			if (preparedStatementUpdate != null) {
				preparedStatementUpdate.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}

	}      //
		return output;
}

	public static void main(String[] args) {
		initTest();
		//
	}
}



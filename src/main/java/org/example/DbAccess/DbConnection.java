package org.example.DbAccess;

import java.sql.*;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.IOException;
import java.util.Vector;

import net.ucanaccess.converters.TypesMap.AccessType;
import net.ucanaccess.ext.FunctionType;
import net.ucanaccess.jdbc.UcanaccessConnection;
import net.ucanaccess.jdbc.UcanaccessDriver;

import javax.swing.table.DefaultTableModel;


public class DbConnection {

    @FunctionType(functionName = "justconcat", argumentTypes = {AccessType.TEXT, AccessType.TEXT}, returnType = AccessType.TEXT)
    public static String concat(String s1, String s2) {
        return s1 + " >>>>" + s2;
    }

    public static Connection getUcanaccessConnection(String pathNewDB) throws SQLException,
            IOException {
        String url = UcanaccessDriver.URL_PREFIX + pathNewDB + ";newDatabaseVersion=V2003";

        return DriverManager.getConnection(url, "sa", "");
    }

    private Connection ucaConn;

    public DbConnection(String pathNewDB) {
        try {
            this.ucaConn = getUcanaccessConnection(pathNewDB);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void createTablesExample() throws SQLException {
        DatabaseMetaData md = ucaConn.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);
        boolean not_exist = true;
        while (rs.next()) {
            if(rs.getString(3).equals("group1"))
            {
                not_exist = false;
            }
        }
        if(not_exist) {
            Statement st = this.ucaConn.createStatement();
            st.execute("CREATE TABLE group1 (id  COUNTER PRIMARY KEY,fio text(400), record_book_number numeric, birth_date date) ");
            st.close();

            String[] birth_date = new String[]{
                    "01.02.2000", "13.02.2002", "22.02.2002", "12.02.2002", "18.03.2001", "02.04.2003", "28.03.2003",
                    "30.04.2003", "31.05.2002", "05.08.2001", "03.07.2002", "13.05.2003", "26.07.2001", "01.08.2003",
                    "16.09.2002", "09.10.2001", "10.11.2001", "15.12.2002", "29.09.2003", "07.10.2002"
            };

            for (int i = 0; i < birth_date.length; i++) {
                ParsePosition pp1 = new ParsePosition(0);
                Date date = new SimpleDateFormat("dd.MM.yyyy").parse(birth_date[i], pp1);
                insertData(String.format("INSERT INTO group1 (fio, record_book_number, birth_date)  VALUES( 'fio%s', '0', '%s')", i, new java.sql.Date(date.getTime())));
            }
        }
    }
    public void createTable(String sql) throws SQLException {
        Statement st = this.ucaConn.createStatement();
        st.execute(sql);
        st.close();
    }

    public DefaultTableModel executeQuery(String sql) throws SQLException {
        Statement st = null;
        DefaultTableModel dtm;
        try {
            st = this.ucaConn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            dtm = buildTableModel(rs);
        } finally {
            if (st != null)
                st.close();
        }
        return dtm;
    }
    public void insertData(String sql) throws SQLException {
        Statement st = null;
        try {
            st = this.ucaConn.createStatement();
            st.execute(sql);
        } finally {
            if (st != null)
                st.close();
        }
    }

    public void transaction(String sql) throws SQLException {
        Statement st = null;
        try {
            this.ucaConn.setAutoCommit(false);
            st = this.ucaConn.createStatement();
            st.executeUpdate(sql);
            this.ucaConn.commit();
        } finally {
            if (st != null)
                st.close();
        }
    }
    private DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);

    }
}

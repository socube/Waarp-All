/*
 * This file is part of Waarp Project (named also Waarp or GG).
 *
 *  Copyright (c) 2019, Waarp SAS, and individual contributors by the @author
 *  tags. See the COPYRIGHT.txt in the distribution for a full listing of
 * individual contributors.
 *
 *  All Waarp Project is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Waarp is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with
 * Waarp . If not, see <http://www.gnu.org/licenses/>.
 */

package org.waarp.openr66.dao.database.oracle;

import org.waarp.openr66.dao.database.DBTransferDAO;
import org.waarp.openr66.dao.exception.DAOConnectionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OracleTransferDAO extends DBTransferDAO {

  protected static String SQL_GET_ID = "SELECT runseq.nextval FROM DUAL";

  public OracleTransferDAO(Connection con) throws DAOConnectionException {
    super(con);
  }

  @Override
  protected long getNextId() throws DAOConnectionException {
    PreparedStatement ps = null;
    try {
      ps = connection.prepareStatement(SQL_GET_ID);
      final ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return rs.getLong(1);
      } else {
        throw new DAOConnectionException(
            "Error no id available, you should purge the database.");
      }
    } catch (final SQLException e) {
      throw new DAOConnectionException(e);
    } finally {
      closeStatement(ps);
    }
  }
}

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="b361asd.auction.gui.Helper" %>
<%@ page import="static b361asd.auction.db.DBBase.*" %>
<%@ page import="b361asd.auction.gui.TableData" %>

<%
   String __userID = (String) session.getAttribute(SESSION_ATTRIBUTE_USER);
   //
   TableData _dataTable = (TableData) request.getAttribute("dataTable");
   String _uri = request.getRequestURI();
   //
   boolean _listActivity = _uri.toUpperCase().contains("listActivity.jsp".toUpperCase());         //User
   boolean _listMyBid = _uri.toUpperCase().contains("listMyBid.jsp".toUpperCase());               //User
   boolean _listBidForOffer = _uri.toUpperCase().contains("listBidForOffer.jsp".toUpperCase());      //User
   boolean _viewAlertDetail = _uri.toUpperCase().contains("viewAlertDetail.jsp".toUpperCase());      //User
   boolean _listBid = _uri.toUpperCase().contains("listBid.jsp".toUpperCase());                  //Rep
   boolean _modifyBid = _uri.toUpperCase().contains("modifyBid.jsp".toUpperCase());               //Rep
   //
   int addCol1st = 0;
   int addCol2nd = 0;
   if (_listActivity) {
      addCol1st = 1;
      //addCol2nd = 0;
   }
   else if (_listMyBid) {
      addCol1st = 1;
      //addCol2nd = 0;
   }
   else if (_listBidForOffer) {
      //addCol1st = 0;
      //addCol2nd = 0;
   }
   else if (_viewAlertDetail) {
      addCol1st = 1;
      //addCol2nd = 0;
   }
   else if (_listBid) {
      //addCol1st = 0;
      addCol2nd = 1;
   }
   else if (_modifyBid) {
      //addCol1st = 0;
      //addCol2nd = 0;
   }
%>

<br/>
<br/>
<%
   if (_dataTable != null) {
%>

<table>
   <thead>
   <%
      out.println("<tr>");
      out.println(_dataTable.printDescriptionForTable((addCol1st > 0)));
      out.println("</tr>");
      out.println("<tr>");
      if (addCol1st == 1) {
         out.println("<td>Action</td>");
      }
      out.println(_dataTable.printHeaderForTable());
      out.println("</tr>");
      //
      if (_dataTable.rowCount() > 0) {
         for (int i = 0; i < _dataTable.rowCount(); i++) {
            TableData _dataTableBid = (TableData) (_dataTable.getLastCellInRow(i));
            if (_dataTableBid != null) {
               out.println("<tr>");
               out.println("<td></td>");                                                //first is empty cell
               out.println("<td>Bids</td>");                                             //Second is "Bids"
               {
                  out.println("<td colspan='" + (_dataTable.colCount() + addCol1st - 2) + "'>");      //Add Addition, minus the above 2
                  //
                  out.println("<table width='100%'>");
                  out.println("<thead>");
                  out.println("<tr>");
                  if (addCol2nd == 1) {
                     out.println("<td>Action</td>");
                  }
                  out.println(_dataTableBid.printSubHeaderForTable());
                  out.println("</tr>");
                  out.println("</thead>");
                  out.println("</table>");
                  //
                  out.println("</td>");
               }
               out.println("</tr>");
               //
               break;
            }
         }
      }
   %>
   </thead>
   <tbody>
   <%
      if (_dataTable.rowCount() > 0) {
         for (int i = 0; i < _dataTable.rowCount(); i++) {
            out.println(_dataTable.printRowStart(i));
            if (addCol1st == 1) {
               out.println("<td>");
               if (_listActivity || _viewAlertDetail || _listMyBid) {
                  String _seller = _dataTable.getOneCell(i, 1).toString();
                  String _status = _dataTable.getOneCell(i, 10).toString();
                  if (!__userID.equalsIgnoreCase(_seller) && _status.equalsIgnoreCase("Active")) {         //status
                     out.println(Helper.getButton("form-id-doBid", "input-id-doBid", "" + _dataTable.getOneCell(i, 0), "Bid"));
                  }
                  out.println(Helper.getButton("form-id-listSimilar", "input-id-listSimilar", _dataTable.getOneCell(i, 0) + "," + _dataTable.getOneCell(i, 2) + "," + _dataTable.getOneCell(i, 3), "Similars"));
               }
               else if (_listBidForOffer) {
                  out.println("NEVER");
               }
               else if (_listBid) {
                  out.println("NEVER");
               }
               else if (_modifyBid) {
                  out.println("NEVER");
               }
               out.println("</td>");
            }
            //
            out.println(_dataTable.printOneRowInTable(i));
            out.println("</tr>");
            //
            TableData _dataTableBid = (TableData) (_dataTable.getLastCellInRow(i));
            if (_dataTableBid != null && _dataTableBid.rowCount() > 0) {
               out.println("<tr>");
               out.println("<td></td>");
               out.println("<td>Bids</td>");
               {
                  out.println("<td colspan='" + (_dataTable.colCount() + addCol1st - 2) + "'>");
                  //
                  out.println("<table width='100%'>");
                  out.println("<tbody>");
                  for (int j = 0; j < _dataTableBid.rowCount(); j++) {
                     out.println(_dataTableBid.printRowStart(j));
                     //
                     if (addCol2nd == 1) {
                        out.println("<td>");
                        if (_listActivity) {
                           out.println("NEVER");
                        }
                        else if (_listMyBid) {
                           out.println("NEVER");
                        }
                        else if (_listBidForOffer) {
                           out.println("NEVER");
                        }
                        else if (_viewAlertDetail) {
                           out.println("NEVER");
                        }
                        else if (_listBid) {
                           out.println(Helper.getButton("form-id-cancelBid", "input-id-cancelBid", _dataTableBid.getOneCell(j, 0) + "", "Cancel Bid"));
                           out.println(Helper.getButton("form-id-modifyBid", "input-id-modifyBid", _dataTableBid.getOneCell(j, 0) + "," + _dataTableBid.getOneCell(j, 1) + "," + _dataTableBid.getOneCell(j, 2), "Modify Bid"));
                           //out.println("<button onclick=\"document.getElementById('input-id-cancelBid').value='" + _dataTableBid.getOneCell(j, 0) + "'; document.getElementById('form-id-cancelBid').submit();\" class=\"favorite styled\" type=\"button\">Cancel Bid</button>");
                           //out.println("<button onclick=\"document.getElementById('input-id-modifyBid').value='" + _dataTableBid.getOneCell(j, 0) + "," + _dataTableBid.getOneCell(j, 1) + "," + _dataTableBid.getOneCell(j, 2) + "'; document.getElementById('form-id-modifyBid').submit();\" class=\"favorite styled\" type=\"button\">Modify Bid</button>");
                        }
                        else if (_modifyBid) {
                           out.println("NEVER");
                        }
                        out.println("</td>");
                     }
                     //
                     out.println(_dataTableBid.printOneRowInTable(j));
                     out.println("</tr>");
                  }
                  out.println("</tbody>");
                  out.println("</table>");
                  //
                  out.println("</td>");
               }
               out.println("</tr>");
            }
         }
      }
   %>
   </tbody>
</table>


<%
   }
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%-- <%@ page import="vn.nganluong.naba.channel.vib.dto.VIBConst" %> --%>

<spring:htmlEscape defaultHtmlEscape="false" />

<style>

textarea
{
  width:100%;
}
.textwrapper
{
  border:1px solid #999999;
  margin:5px 0;
  padding:3px;
}
</style>

    <!-- Content Header (Page header) -->
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>Lịch sử thanh toán</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin/dashboard/dashboard1">Trang chủ</a></li>
              <li class="breadcrumb-item active">Lịch sử thanh toán</li>
            </ol>
          </div>
        </div>
      </div><!-- /.container-fluid -->
    </section>
<form:form action="${pageContext.request.contextPath}/admin/payment/search"
					modelAttribute="paymentSearchDto" id="paymentForm"
					method="POST">
    <!-- Main content -->
    <section class="content">
      <div class="container-fluid">
        
        <div class="row">
          <div class="col-12">
            <div class="card">
              <div class="card-header">
                <h3 class="card-title">Nhập thông tin tìm kiếm</h3>

                <!-- <div class="card-tools">
                  <div class="input-group input-group-sm" style="width: 150px;">
                    <input type="text" name="table_search" class="form-control float-right" placeholder="Search">

                    <div class="input-group-append">
                      <button type="submit" class="btn btn-default"><i class="fas fa-search"></i></button>
                    </div>
                  </div>
                </div> -->
              </div>
              <!-- /.card-header -->
              
           
              <div class="card-body">
	            <div class="row">
	              <div class="col-md-6">
	                <div class="form-group">
	                  <label>Trace ID</label>
	                  <form:input type="text" class="form-control" placeholder="Nhập trace id (mã ủy nhiệm chi)" path="clientTransactionId" id="clientTransactionId" />
	                </div>
	                <!-- /.form-group -->
	                <div class="form-group">
	                  <label>Channel</label>	                  
	                  <form:select class="form-control" path="channelCode" id="channelCode">
						   <form:option value="" label="--- Chọn channel ---"/>
						   <form:options items="${channelList}" />
	                  </form:select>
	                </div>
	                <!-- /.form-group -->
	                
	                
	              </div>
	              <!-- /.col -->
	              <div class="col-md-6">
	                <div class="form-group">
	                  <label>Số tài khoản</label>
	                  <form:input type="text" class="form-control" placeholder="Nhập số tài khoản" path="accountNo" id="accountNo" />
	                </div>
	                
	                <div class="form-group">
	                	<form:hidden path="fromDate" name="fromDate" />
	                	<form:hidden path="toDate" name="toDate" />
	                  <label>Khoảng thời gian</label>
						
	                  <div class="input-group">
	                    <div class="input-group-prepend">
	                      <span class="input-group-text"><i class="far fa-clock"></i></span>
	                    </div>
	                    <input type="text" class="form-control float-right" id="reservationtime">
	                  </div>
	                  <!-- /.input group -->
	                </div>
	                <!-- /.form-group -->
	                
	                <!-- <div class="row">
	                	<div class="col-md-6">
	                		<div class="form-group">
			                  <label>Từ ngày</label>
			                  <input type="text" class="form-control" placeholder="Nhập thời gian bắt đầu">
							</div>
	                	</div>
	                
	                	<div class="col-md-6">
	                		<div>
			                  <label>Đến ngày</label>
			                  <input type="text" class="form-control" placeholder="Nhập thời gian kết thúc">
			                </div>
	                	</div>
	                </div> -->
	                
	              </div>
	              <!-- /.col -->
	            </div>
	            
	            <div class="row">
	            
	            	<div class="form-group">
            			<button type="button" class="btn btn-primary" onclick="submitForm(1);">Tìm kiếm</button>	
            		</div>
	            	&emsp;
	            	<div class="form-group">
            			<button type="button" class="btn btn-light" onclick="clearForm();">Xóa tìm kiếm</button>	
            		</div>
	            	
	            	<!-- <div class="col-md-1">
	            		<div class="form-group">
	            			<button type="submit" class="btn btn-primary">Tìm kiếm</button>	
	            		</div>
	            	</div>
	            	<div class="col-md-1">
	            		<div class="form-group">
	            			<button type="button" class="btn btn-secondary">Xóa</button>	
	            		</div>
	            	</div> -->
	            </div>
	            <!-- /.row -->
	          </div>
	          <!-- /.card-body -->
	          <!-- <div class="card-footer">
                  <button type="submit" class="btn btn-primary">Tìm kiếm</button>
              </div> -->
          
              
               <div class="card-header">
                <h3 class="card-title">Hiển thị kết quả tìm kiếm</h3>
              </div>
              <!-- /.card-header -->
              <div class="card-body table-responsive p-0"> <!-- style="height: 550px;" -->
                <table class="table table-head-fixed table-hover text-nowrap">
                  <thead>
                    <tr>
                      <th>#</th>
                      <th>Tên Channel</th>
                      <th>Số tài khoản/ Thẻ</th>
                      <th>MID</th>
                      <th>Trace id</th>
                      <th>Channel trans id</th>
                      <th>Số tiền</th>
                      <th>Thời gian</th>
                      <th>Trạng thái Channel</th>
                      <th>Trạng thái Client</th>
                      <th>Trạng thái Gateway</th>
                      <th>Chi tiết</th>
                    </tr>
                  </thead>
                  <tbody>
                  	<c:forEach items="${payments}" var="payment" varStatus="itr">
                  	<c:choose>
					    <c:when test="${(fn:contains(payment.channelTransactionStatusDisplay, 'SU') or fn:contains(payment.channelTransactionStatusDisplay, 'COM'))
					    		and (fn:contains(payment.merchantTransactionStatusDisplay, 'SU') or fn:contains(payment.merchantTransactionStatusDisplay, 'COM'))}">

							<c:choose>
								<c:when test="${payment.revertStatus == 1 }">
									<tr class="bg-secondary">
								</c:when>
								<c:otherwise>
									<tr class="bg-success">
								</c:otherwise>
							</c:choose>
					    </c:when>

					    <c:when test="${fn:contains(payment.channelTransactionStatusDisplay, 'FAI') or fn:contains(payment.merchantTransactionStatusDisplay, 'FAI') or fn:contains(payment.pgTransactionStatusDisplay, 'FAI')}">
							<tr class="bg-danger">
					    </c:when>

					    <c:when test="${fn:contains(payment.channelTransactionStatusDisplay, 'CAN') or fn:contains(payment.merchantTransactionStatusDisplay, 'CAN') or fn:contains(payment.pgTransactionStatusDisplay, 'CAN')}">
							<tr class="bg-warning">
					    </c:when>

						<c:when test="${payment.revertStatus == 1 }">
							<tr class="bg-secondary">
						</c:when>

					    <c:otherwise>
 						<tr>
					    </c:otherwise>
					</c:choose>
			            
			                <td>${paginationProducts.fromRecord + itr.index}</td>
			                <td>${payment.channelName }</td>
			                <td>${payment.accountNo }</td>
			                <td>${payment.merchantName }</td>
			                <td>${payment.merchantTransactionId }</td>
			                <td>${payment.channelTransactionId }</td>
			                <td class="text-right">${payment.amount }</td>
			                <td>${payment.timeCreatedFormat }</td>
			                <td>${payment.channelTransactionStatusDisplay }
								<c:if test="${payment.revertStatus == 1 }">
									<br> Hoàn tiền
									<i class="fas fa-undo"></i>
								</c:if>
							</td>
			                <td>${payment.merchantTransactionStatusDisplay }</td>
			                <td>${payment.pgTransactionStatusDisplay }</td>
			                <td class="bg-light">
			                	<a class="btn btn-light btn-xs" data-toggle="modal" data-target="#modal-default" onclick="getDetailPayment(${payment.id});" ><i class="fas fa-eye" aria-hidden="true"></i></a>
			                </td>
			            </tr>
			        </c:forEach>
                  	
                    <!-- <tr>
                      <td>183</td>
                      <td>John Doe</td>
                      <td>11-7-2014</td>
                      <td><span class="tag tag-success">Approved</span></td>
                      <td>Bacon ipsum dolor sit amet salami venison chicken flank fatback doner.</td>
                    </tr> -->
                  </tbody>
                </table>
              </div>
              <!-- /.card-body -->
              
		<%-- <c:if test="${paginationProducts.totalPages > 1}">
			   <div class="page-navigator">
			      <c:forEach items="${paginationProducts.navigationPages}" var = "page">
			         <c:if test="${page != -1 }">
			            <a href="index?page=${page}" class="nav-item">${page}</a>
			         </c:if>
			         <c:if test="${page == -1 }">
			            <span class="nav-item"> ... </span>
			         </c:if>
			      </c:forEach>
			   </div>
              </c:if> --%>
              
              <form:hidden path="pageOfList" name="pageOfList" value="${paginationProducts.currentPage}" />
              <c:if test="${paginationProducts.totalPages > 1}">
	              <div class="card-footer clearfix">
	              	Hiển thị từ ${paginationProducts.fromRecord} tới ${paginationProducts.toRecord} trong số ${paginationProducts.totalRecords} bản ghi.
	              
	                <ul class="pagination pagination-sm m-0 float-right">
	                			<li class="page-item"><a class="page-link" href="javascript:submitForm(1);">&laquo;</a></li>
	                	<c:forEach items="${paginationProducts.navigationPages}" var = "page">
					         <c:if test="${page != -1 }">
					         	<c:if test="${page != paginationProducts.currentPage }">
					         		<li class="page-item"><a class="page-link" href="javascript:submitForm(${page});">${page}</a></li>
					         	</c:if>
					         	
					         	<c:if test="${page == paginationProducts.currentPage }">
					         		<li class="page-item"><a class="page-link disabled text-dark" href="javascript:function() { return false; }" >${page}</a></li>
					         	</c:if>		
					            
					         </c:if>
					         <c:if test="${page == -1 }">
					            <li class="page-item"><a class="page-link" href="javascript:function() { return false; }">...</a></li>
					         </c:if>
				      	</c:forEach>
				      			<li class="page-item"><a class="page-link" href="javascript:submitForm(${paginationProducts.totalPages});">&raquo;</a></li>
	                </ul>
	              </div>
              </c:if>
              
              <!-- <div class="card-footer clearfix">
                <ul class="pagination pagination-sm m-0 float-right">
                  <li class="page-item"><a class="page-link" href="#">&laquo;</a></li>
                  <li class="page-item"><a class="page-link" href="#">1</a></li>
                  <li class="page-item"><a class="page-link" href="#">2</a></li>
                  <li class="page-item"><a class="page-link" href="#">3</a></li>
                  <li class="page-item"><a class="page-link" href="#">&raquo;</a></li>
                </ul>
              </div> -->
              
            </div>
            <!-- /.card -->
          </div>
        </div>
        <!-- /.row -->
        
      </div><!-- /.container-fluid -->
    </section>
    <!-- /.content -->
    
    <div class="modal fade" id="modal-default">
        <div class="modal-dialog modal-lg">
          <div class="modal-content">
            <div class="modal-header">
              <h4 class="modal-title">Thông tin chi tiết thanh toán</h4>
              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
				<div class="row">
	              <div class="col-md-3">
	                  Tên channel
	                
	              </div>
	              <div class="col-md-9">
	                  <span id="detail_channelName"></span>
	              </div>
				</div>       
				<div class="row">
	              <div class="col-md-3">
	                  Số tài khoản/ Thẻ
	                
	              </div>
					<div class="col-md-3">
						MID

					</div>
	              <div class="col-md-9">
	                  <!-- <span id="detail_accountNo"></span> -->
	                  <span id="detail_accountNo"></span>
	              </div>
				</div>         
				<div class="row">
	              <div class="col-md-3">
	                  Trace id
	                
	              </div>
	              <div class="col-md-9">
	                  <span id="detail_traceId"></span>
	              </div>
				</div>         
				<div class="row">
	              <div class="col-md-3">
	                  Channel trans id
	                
	              </div>
	              <div class="col-md-9">
	                  <span id="detail_channelTransactionId"></span>
	              </div>
				</div>
				
				<div class="row">
	              <div class="col-md-3">
	                  Số tiền
	              </div>
	              <div class="col-md-9">
	                  <span id="detail_amount"></span>
	              </div>
				</div>
				
				<div class="row">
	              <div class="col-md-3">
	                  Thời gian giao dịch
	              </div>
	              <div class="col-md-9">
	                  <span id="detail_timeCreated"></span>
	              </div>
				</div>
				
				<div class="row">
	              <div class="col-md-3">
	                  Trạng thái channel
	              </div>
	              <div class="col-md-9">
	                  <span id="detail_channelTransactionStatus" class=""></span>
	              </div>
				</div>
				
				<div class="row">
	              <div class="col-md-3">
	                  Trạng thái client
	              </div>
	              <div class="col-md-9">
	                  <span id="detail_merchantTransactionStatus" class=""></span>
	              </div>
				</div>
				
				<div class="row">
	              <div class="col-md-3">
	                  Trạng thái gateway
	              </div>
	              <div class="col-md-9">
	                  <span id="detail_pgTransactionStatus" class=""></span>
	              </div>
				</div>
				
				<div class="row">
	              <div class="col-md-3">
	                  Request raw
	              </div>
	              <div class="col-md-9">
	                  <div class="textwrapper">
	                  	<textarea id="detail_rawRequest" rows="10"></textarea>
	                  </div>
	              </div>
				</div>
				
				<div class="row">
	              <div class="col-md-3">
	                  Response raw
	              </div>
	              <div class="col-md-9">
	                  <div class="textwrapper">
	                  	<textarea id="detail_rawResponse" rows="10"></textarea>
	                  </div>
	              </div>
				</div>

            </div>
            <div class="modal-footer justify-content-between">
              <button type="button" class="btn btn-default" data-dismiss="modal" style="visibility: hidden;" >Đóng</button>
              <button type="button" class="btn btn-default" data-dismiss="modal">Đóng</button>
            </div>
          </div>
          <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
      </div>
      <!-- /.modal -->
    
</form:form>

<script type="text/javascript">

$(document).ready(function() {
	
	document.getElementById("imPayment").classList.add('active');
	
	// https://www.daterangepicker.com/#options
	$('#reservationtime').daterangepicker({
	      timePicker: true,
	      "timePicker24Hour": true,
	      "autoApply": true,
	      /* timePickerIncrement: 30, */
	      locale: {
	        format: 'YYYY-MM-DD HH:mm:ss',
	        cancelLabel: 'Clear'
	      }
    });
	
	$('#reservationtime').on('apply.daterangepicker', function(ev, picker) {
		$("#fromDate").val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
		$("#toDate").val(picker.endDate.format('YYYY-MM-DD HH:mm:ss'));
	});
	$('#reservationtime').on('cancel.daterangepicker', function(ev, picker) {
		$(this).val('');
	});
	
	updateDatePicker();
});

function submitForm(page){
	$("#pageOfList").val(page);
	$("#paymentForm").submit();
};

function updateDatePicker(){
	var startDate = $("#fromDate").val();
	var endDate = $("#toDate").val();
	
	if (startDate && endDate) {
		$('#reservationtime').data('daterangepicker').setStartDate(startDate);
		
		var endDateExist = $('#reservationtime').data('daterangepicker').endDate.format('YYYY-MM-DD HH:mm:ss');
		if (endDateExist != endDate){
			$('#reservationtime').data('daterangepicker').setEndDate(endDate);
		}
	}
	else if (!startDate && !endDate) {
		$('#reservationtime').val('');
	}
	else {
		$("#fromDate").val($('#reservationtime').data('daterangepicker').startDate.format('YYYY-MM-DD HH:mm:ss'));
		$("#toDate").val($('#reservationtime').data('daterangepicker').endDate.format('YYYY-MM-DD HH:mm:ss'));
	}
	
	
}

function clearForm(){
	var elements = document.getElementsByTagName("input");
	for (var ii=0; ii < elements.length; ii++) {
	  if (elements[ii].type == "text") {
	    elements[ii].value = "";
	  }
	}
	$("#fromDate").val('');
	$("#toDate").val('');

	$("#channelCode").val('');
}

function getDetailPayment(id){
	
	document.getElementById("detail_channelName").innerHTML = "";
	document.getElementById("detail_accountNo").innerHTML = "";
	document.getElementById("detail_traceId").innerHTML = "";
	document.getElementById("detail_channelTransactionId").innerHTML = "";
	document.getElementById("detail_amount").innerHTML = "";
	document.getElementById("detail_timeCreated").innerHTML = "";
	document.getElementById("detail_channelTransactionStatus").innerHTML = "";
	document.getElementById("detail_merchantTransactionStatus").innerHTML = "";
	document.getElementById("detail_pgTransactionStatus").innerHTML = "";
	
	$("#detail_rawRequest").val("");
	$("#detail_rawResponse").val("");
	
	$.ajax({
        url: "${pageContext.request.contextPath}/admin/payment/detail/" + id,
        ajaxasync: true,
        success: function (response) {
            successHandle(response);
        },
        error: function (xhr) {
            alert("Có lỗi trong quá trình xử lý")
        }
    });
}

function successHandle(response) {
    
	document.getElementById("detail_channelName").innerHTML = response.channelName;
	document.getElementById("detail_accountNo").innerHTML = response.accountNo;
	document.getElementById("detail_traceId").innerHTML = response.merchantTransactionId;
	document.getElementById("detail_channelTransactionId").innerHTML = response.channelTransactionId;
	document.getElementById("detail_amount").innerHTML = response.amount;
	document.getElementById("detail_timeCreated").innerHTML = response.timeCreatedFormat;

	document.getElementById("detail_merchantTransactionStatus").innerHTML = response.merchantTransactionStatusDisplay;
	document.getElementById("detail_pgTransactionStatus").innerHTML = response.pgTransactionStatusDisplay;

	if (response.revertStatus == 1){
		var refundDisplay = "<span class='bg-secondary'> Hoàn tiền <i class='fas fa-undo'></i> </span>";
		// Hoàn tiền
		// <i class="fas fa-undo"></i>
		document.getElementById("detail_channelTransactionStatus").innerHTML = response.channelTransactionStatusDisplay + refundDisplay;
	}
	else {
		document.getElementById("detail_channelTransactionStatus").innerHTML = response.channelTransactionStatusDisplay;
	}
	
	$("#detail_rawRequest").val(response.rawRequest);
	$("#detail_rawResponse").val(response.rawResponse);
	
	addClassStatus(response.channelTransactionStatusDisplay, $("#detail_channelTransactionStatus"));
	addClassStatus(response.merchantTransactionStatusDisplay, $("#detail_merchantTransactionStatus"));
	addClassStatus(response.pgTransactionStatusDisplay, $("#detail_pgTransactionStatus"));
}

function addClassStatus(status, objectElement){
	objectElement.removeClass("bg-success");
	objectElement.removeClass("bg-danger");
	objectElement.removeClass("bg-warning");
	if (status.indexOf("COM") !== -1 || status.indexOf("SUC")  !== -1){
		objectElement.addClass("bg-success");
	}
	else if (status.indexOf("FAI") !== -1){
		objectElement.addClass("bg-danger");
	}
	else if (status.indexOf("CAN") !== -1) {
		objectElement.addClass("bg-warning");
	}
}

</script>

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
            <h1>Danh sách Schedule</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin/dashboard/dashboard1">Trang chủ</a></li>
              <li class="breadcrumb-item active">Danh sách schedule</li>
            </ol>
          </div>
        </div>
      </div><!-- /.container-fluid -->
    </section>
<form:form action="${pageContext.request.contextPath}/admin/schedule/search"
					modelAttribute="scheduleSearchDto" id="scheduleForm"
					method="POST">
    <!-- Main content -->
    <section class="content">
      <div class="container-fluid">
        
        <div class="row">
          <div class="col-12">
            <div class="card">
              <div class="card-header">
                <h3 class="card-title">Nhập thông tin tìm kiếm</h3>

              </div>
              <!-- /.card-header -->
              
           
              <div class="card-body">
	            <div class="row">
	              <div class="col-md-6">
	                <div class="form-group">
	                  <label>Channel</label>
	                  <%-- <form:select class="form-control" path="channelId" items="${channelList}" /> --%>
	                  
	                  <form:select class="form-control" path="channelId">
						   <form:option value="" label="--- Chọn channel ---"/>
						   <form:options items="${channelList}" />
	                  </form:select>
	                </div>
	                <!-- /.form-group -->
	              </div>
	              <!-- /.col -->
	              <div class="col-md-6">
	                <div class="form-group">
	                  <label>Cron expression</label>
	                  <form:input type="text" class="form-control" placeholder="Nhập cron expression" path="cron_expression" id="cron_expression" />
	                </div>
	                 <!-- /.form-group -->
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
                      <th>Tên chức năng</th>
                      <th>Tên schedule</th>
                      <th>Cron expression</th>
                      <th>Mô tả</th>
                      <th>Thời gian tạo</th>
                      <!-- <th>Chi tiết</th> -->
                    </tr>
                  </thead>
                  <tbody>
                  	<c:forEach items="${schedules}" var="schedule" varStatus="itr">
			            <tr>                  	
			            
			                <td>${paginationProducts.fromRecord + itr.index}</td>
			                <td>${schedule.channelName }</td>
			                <td>${schedule.functionName }</td>
			                <td>${schedule.scheduleName }</td>
			                <td>${schedule.cronExpression }</td>
			                <td>${schedule.description }</td>
			                <td>${schedule.timeCreated }</td>
			                <%-- <td class="bg-light">
			                	<a class="btn btn-light btn-xs" data-toggle="modal" data-target="#modal-default" onclick="getDetailPayment(${schedule.id});" ><i class="fas fa-eye" aria-hidden="true"></i></a>
			                </td> --%>
			            </tr>
			        </c:forEach>
                  	
                  </tbody>
                </table>
              </div>
              <!-- /.card-body -->
              
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
              
              
            </div>
            <!-- /.card -->
          </div>
        </div>
        <!-- /.row -->
        
      </div><!-- /.container-fluid -->
    </section>
    <!-- /.content -->
    
</form:form>

<script type="text/javascript">

$(document).ready(function() {
	document.getElementById("imSchedule").classList.add('active');
});

function submitForm(page){
	$("#pageOfList").val(page);
	$("#scheduleForm").submit();
};


function clearForm(){
	var elements = document.getElementsByTagName("input");
	for (var ii=0; ii < elements.length; ii++) {
	  if (elements[ii].type == "text") {
	    elements[ii].value = "";
	  }
	}
	$("#channelId").val('');
}




</script>

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
            <h1>Xóa bộ nhớ đệm (Cache)</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin">Trang chủ</a></li>
              <li class="breadcrumb-item active">Xóa bộ nhớ đệm (Cache)</li>
            </ol>
          </div>
        </div>
      </div><!-- /.container-fluid -->
    </section>
<form:form action="${pageContext.request.contextPath}/admin/cache"
					modelAttribute="cacheRequestDto" id="cacheForm"
					method="POST">
    <!-- Main content -->
    <section class="content">
      <div class="container-fluid">
        
        <div class="row">
          <div class="col-12">
            <div class="card">
              <!-- <div class="card-header">
                <h3 class="card-title">Nhập thông tin tìm kiếm</h3>

              </div> -->
              <!-- /.card-header -->
              
           
              <div class="card-body">
	            <div class="row" style="height: 280px;">
	              <div class="col-md-2">
	                <div class="form-group">
	                  <label>Lựa chọn bảng</label>
	                </div>
	                <!-- /.form-group -->
	                
	              </div>
	              <!-- /.col -->
	              <div class="col-md-8">
	                <div class="form-group">
	                  <form:hidden path="requestType" name="requestType" />
	                  <form:select id="entitiesSelect" path = "entitiesSelect" items = "${entitiesList}" multiple = "true" cssStyle="height:250px;" />
	                </div>
	                
	                
	              </div>
	              <!-- /.col -->
	            </div>
	            
	            <div class="row">
	            
	            	<div class="form-group">
            			<button type="button" class="btn btn-primary" onclick="submitForm(1);">Xóa tất cả</button>	
            		</div>
	            	&emsp;
	            	<div class="form-group">
            			<button type="button" class="btn btn-primary" onclick="submitForm(2);">Xóa tùy chọn</button>	
            		</div>
	            	&emsp;
	            	<div class="form-group">
            			<button type="button" class="btn btn-light" onclick="clearForm();">Clear input</button>	
            			
            			<form:hidden path="resultMessage" name="resultMessage" id="resultMessage" />
            		</div>
	            	
	            </div>
	            <!-- /.row -->
	          </div>
	          <!-- /.card-body -->
          
              
              <!-- /.card-header -->
              
              
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
              <h4 class="modal-title">Thông báo</h4>
              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
				<div class="row">
	              <span id="msgResult"></span>
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
	
	document.getElementById("imCache").classList.add('active');
	// document.getElementById("imPayment").classList.remove('active');
	
	var checkMsg = $("#resultMessage").val();
	if (checkMsg) {
		
		document.getElementById("msgResult").innerHTML = checkMsg;
		$('#modal-default').modal('show');
	}
	
	
});

function submitForm(requestType){
	$("#requestType").val(requestType);
	$("#cacheForm").submit();
};

function clearForm(){
	$("#entitiesSelect").val([]);
}


</script>

/**
 * VcbNotifyRes.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package clientTest.vcb_ecom;

public class VcbNotifyRes implements java.io.Serializable {
    private String data;

    private String partnerId;

    private String signature;

    private String transactionId;

    public VcbNotifyRes() {
    }

    public VcbNotifyRes(
           String data,
           String partnerId,
           String signature,
           String transactionId) {
           this.data = data;
           this.partnerId = partnerId;
           this.signature = signature;
           this.transactionId = transactionId;
    }


    /**
     * Gets the data value for this VcbNotifyRes.
     *
     * @return data
     */
    public String getData() {
        return data;
    }


    /**
     * Sets the data value for this VcbNotifyRes.
     *
     * @param data
     */
    public void setData(String data) {
        this.data = data;
    }


    /**
     * Gets the partnerId value for this VcbNotifyRes.
     *
     * @return partnerId
     */
    public String getPartnerId() {
        return partnerId;
    }


    /**
     * Sets the partnerId value for this VcbNotifyRes.
     *
     * @param partnerId
     */
    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }


    /**
     * Gets the signature value for this VcbNotifyRes.
     *
     * @return signature
     */
    public String getSignature() {
        return signature;
    }


    /**
     * Sets the signature value for this VcbNotifyRes.
     *
     * @param signature
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }


    /**
     * Gets the transactionId value for this VcbNotifyRes.
     *
     * @return transactionId
     */
    public String getTransactionId() {
        return transactionId;
    }


    /**
     * Sets the transactionId value for this VcbNotifyRes.
     *
     * @param transactionId
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof VcbNotifyRes)) return false;
        VcbNotifyRes other = (VcbNotifyRes) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true &&
            ((this.data==null && other.getData()==null) ||
             (this.data!=null &&
              this.data.equals(other.getData()))) &&
            ((this.partnerId==null && other.getPartnerId()==null) ||
             (this.partnerId!=null &&
              this.partnerId.equals(other.getPartnerId()))) &&
            ((this.signature==null && other.getSignature()==null) ||
             (this.signature!=null &&
              this.signature.equals(other.getSignature()))) &&
            ((this.transactionId==null && other.getTransactionId()==null) ||
             (this.transactionId!=null &&
              this.transactionId.equals(other.getTransactionId())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getData() != null) {
            _hashCode += getData().hashCode();
        }
        if (getPartnerId() != null) {
            _hashCode += getPartnerId().hashCode();
        }
        if (getSignature() != null) {
            _hashCode += getSignature().hashCode();
        }
        if (getTransactionId() != null) {
            _hashCode += getTransactionId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VcbNotifyRes.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://vcb_ecom.channel.pg/", "vcbNotifyRes"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("data");
        elemField.setXmlName(new javax.xml.namespace.QName("", "data"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("partnerId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "partnerId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("signature");
        elemField.setXmlName(new javax.xml.namespace.QName("", "signature"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "transactionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}

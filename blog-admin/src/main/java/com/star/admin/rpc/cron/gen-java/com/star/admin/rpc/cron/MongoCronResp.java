/**
 * Autogenerated by Thrift Compiler (0.18.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.star.admin.rpc.cron;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.18.0)", date = "2023-02-25")
public class MongoCronResp implements org.apache.thrift.TBase<MongoCronResp, MongoCronResp._Fields>, java.io.Serializable, Cloneable, Comparable<MongoCronResp> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("MongoCronResp");

  private static final org.apache.thrift.protocol.TField LEN_FIELD_DESC = new org.apache.thrift.protocol.TField("len", org.apache.thrift.protocol.TType.I64, (short)1);
  private static final org.apache.thrift.protocol.TField OK_FIELD_DESC = new org.apache.thrift.protocol.TField("ok", org.apache.thrift.protocol.TType.BOOL, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new MongoCronRespStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new MongoCronRespTupleSchemeFactory();

  public long len; // required
  public boolean ok; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    LEN((short)1, "len"),
    OK((short)2, "ok");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // LEN
          return LEN;
        case 2: // OK
          return OK;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    @Override
    public short getThriftFieldId() {
      return _thriftId;
    }

    @Override
    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __LEN_ISSET_ID = 0;
  private static final int __OK_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.LEN, new org.apache.thrift.meta_data.FieldMetaData("len", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.OK, new org.apache.thrift.meta_data.FieldMetaData("ok", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(MongoCronResp.class, metaDataMap);
  }

  public MongoCronResp() {
  }

  public MongoCronResp(
    long len,
    boolean ok)
  {
    this();
    this.len = len;
    setLenIsSet(true);
    this.ok = ok;
    setOkIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public MongoCronResp(MongoCronResp other) {
    __isset_bitfield = other.__isset_bitfield;
    this.len = other.len;
    this.ok = other.ok;
  }

  @Override
  public MongoCronResp deepCopy() {
    return new MongoCronResp(this);
  }

  @Override
  public void clear() {
    setLenIsSet(false);
    this.len = 0;
    setOkIsSet(false);
    this.ok = false;
  }

  public long getLen() {
    return this.len;
  }

  public MongoCronResp setLen(long len) {
    this.len = len;
    setLenIsSet(true);
    return this;
  }

  public void unsetLen() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __LEN_ISSET_ID);
  }

  /** Returns true if field len is set (has been assigned a value) and false otherwise */
  public boolean isSetLen() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __LEN_ISSET_ID);
  }

  public void setLenIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __LEN_ISSET_ID, value);
  }

  public boolean isOk() {
    return this.ok;
  }

  public MongoCronResp setOk(boolean ok) {
    this.ok = ok;
    setOkIsSet(true);
    return this;
  }

  public void unsetOk() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __OK_ISSET_ID);
  }

  /** Returns true if field ok is set (has been assigned a value) and false otherwise */
  public boolean isSetOk() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __OK_ISSET_ID);
  }

  public void setOkIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __OK_ISSET_ID, value);
  }

  @Override
  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case LEN:
      if (value == null) {
        unsetLen();
      } else {
        setLen((java.lang.Long)value);
      }
      break;

    case OK:
      if (value == null) {
        unsetOk();
      } else {
        setOk((java.lang.Boolean)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case LEN:
      return getLen();

    case OK:
      return isOk();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  @Override
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case LEN:
      return isSetLen();
    case OK:
      return isSetOk();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof MongoCronResp)
      return this.equals((MongoCronResp)that);
    return false;
  }

  public boolean equals(MongoCronResp that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_len = true;
    boolean that_present_len = true;
    if (this_present_len || that_present_len) {
      if (!(this_present_len && that_present_len))
        return false;
      if (this.len != that.len)
        return false;
    }

    boolean this_present_ok = true;
    boolean that_present_ok = true;
    if (this_present_ok || that_present_ok) {
      if (!(this_present_ok && that_present_ok))
        return false;
      if (this.ok != that.ok)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(len);

    hashCode = hashCode * 8191 + ((ok) ? 131071 : 524287);

    return hashCode;
  }

  @Override
  public int compareTo(MongoCronResp other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetLen(), other.isSetLen());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLen()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.len, other.len);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetOk(), other.isSetOk());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetOk()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.ok, other.ok);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  @Override
  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  @Override
  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("MongoCronResp(");
    boolean first = true;

    sb.append("len:");
    sb.append(this.len);
    first = false;
    if (!first) sb.append(", ");
    sb.append("ok:");
    sb.append(this.ok);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class MongoCronRespStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public MongoCronRespStandardScheme getScheme() {
      return new MongoCronRespStandardScheme();
    }
  }

  private static class MongoCronRespStandardScheme extends org.apache.thrift.scheme.StandardScheme<MongoCronResp> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, MongoCronResp struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // LEN
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.len = iprot.readI64();
              struct.setLenIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // OK
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.ok = iprot.readBool();
              struct.setOkIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    @Override
    public void write(org.apache.thrift.protocol.TProtocol oprot, MongoCronResp struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(LEN_FIELD_DESC);
      oprot.writeI64(struct.len);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(OK_FIELD_DESC);
      oprot.writeBool(struct.ok);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class MongoCronRespTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public MongoCronRespTupleScheme getScheme() {
      return new MongoCronRespTupleScheme();
    }
  }

  private static class MongoCronRespTupleScheme extends org.apache.thrift.scheme.TupleScheme<MongoCronResp> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, MongoCronResp struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetLen()) {
        optionals.set(0);
      }
      if (struct.isSetOk()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetLen()) {
        oprot.writeI64(struct.len);
      }
      if (struct.isSetOk()) {
        oprot.writeBool(struct.ok);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, MongoCronResp struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.len = iprot.readI64();
        struct.setLenIsSet(true);
      }
      if (incoming.get(1)) {
        struct.ok = iprot.readBool();
        struct.setOkIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

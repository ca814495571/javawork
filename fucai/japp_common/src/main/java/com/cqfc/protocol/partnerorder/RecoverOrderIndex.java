/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.cqfc.protocol.partnerorder;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecoverOrderIndex implements org.apache.thrift.TBase<RecoverOrderIndex, RecoverOrderIndex._Fields>, java.io.Serializable, Cloneable, Comparable<RecoverOrderIndex> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("RecoverOrderIndex");

  private static final org.apache.thrift.protocol.TField DB_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("dbName", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField ORDER_NO_FIELD_DESC = new org.apache.thrift.protocol.TField("orderNo", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField FLAG_FIELD_DESC = new org.apache.thrift.protocol.TField("flag", org.apache.thrift.protocol.TType.I32, (short)3);
  private static final org.apache.thrift.protocol.TField CREATE_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("createTime", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField LAST_UPDATE_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("lastUpdateTime", org.apache.thrift.protocol.TType.STRING, (short)5);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new RecoverOrderIndexStandardSchemeFactory());
    schemes.put(TupleScheme.class, new RecoverOrderIndexTupleSchemeFactory());
  }

  public String dbName; // required
  public String orderNo; // required
  public int flag; // required
  public String createTime; // required
  public String lastUpdateTime; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    DB_NAME((short)1, "dbName"),
    ORDER_NO((short)2, "orderNo"),
    FLAG((short)3, "flag"),
    CREATE_TIME((short)4, "createTime"),
    LAST_UPDATE_TIME((short)5, "lastUpdateTime");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // DB_NAME
          return DB_NAME;
        case 2: // ORDER_NO
          return ORDER_NO;
        case 3: // FLAG
          return FLAG;
        case 4: // CREATE_TIME
          return CREATE_TIME;
        case 5: // LAST_UPDATE_TIME
          return LAST_UPDATE_TIME;
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
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __FLAG_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.DB_NAME, new org.apache.thrift.meta_data.FieldMetaData("dbName", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.ORDER_NO, new org.apache.thrift.meta_data.FieldMetaData("orderNo", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.FLAG, new org.apache.thrift.meta_data.FieldMetaData("flag", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.CREATE_TIME, new org.apache.thrift.meta_data.FieldMetaData("createTime", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.LAST_UPDATE_TIME, new org.apache.thrift.meta_data.FieldMetaData("lastUpdateTime", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(RecoverOrderIndex.class, metaDataMap);
  }

  public RecoverOrderIndex() {
  }

  public RecoverOrderIndex(
    String dbName,
    String orderNo,
    int flag,
    String createTime,
    String lastUpdateTime)
  {
    this();
    this.dbName = dbName;
    this.orderNo = orderNo;
    this.flag = flag;
    setFlagIsSet(true);
    this.createTime = createTime;
    this.lastUpdateTime = lastUpdateTime;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public RecoverOrderIndex(RecoverOrderIndex other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetDbName()) {
      this.dbName = other.dbName;
    }
    if (other.isSetOrderNo()) {
      this.orderNo = other.orderNo;
    }
    this.flag = other.flag;
    if (other.isSetCreateTime()) {
      this.createTime = other.createTime;
    }
    if (other.isSetLastUpdateTime()) {
      this.lastUpdateTime = other.lastUpdateTime;
    }
  }

  public RecoverOrderIndex deepCopy() {
    return new RecoverOrderIndex(this);
  }

  @Override
  public void clear() {
    this.dbName = null;
    this.orderNo = null;
    setFlagIsSet(false);
    this.flag = 0;
    this.createTime = null;
    this.lastUpdateTime = null;
  }

  public String getDbName() {
    return this.dbName;
  }

  public RecoverOrderIndex setDbName(String dbName) {
    this.dbName = dbName;
    return this;
  }

  public void unsetDbName() {
    this.dbName = null;
  }

  /** Returns true if field dbName is set (has been assigned a value) and false otherwise */
  public boolean isSetDbName() {
    return this.dbName != null;
  }

  public void setDbNameIsSet(boolean value) {
    if (!value) {
      this.dbName = null;
    }
  }

  public String getOrderNo() {
    return this.orderNo;
  }

  public RecoverOrderIndex setOrderNo(String orderNo) {
    this.orderNo = orderNo;
    return this;
  }

  public void unsetOrderNo() {
    this.orderNo = null;
  }

  /** Returns true if field orderNo is set (has been assigned a value) and false otherwise */
  public boolean isSetOrderNo() {
    return this.orderNo != null;
  }

  public void setOrderNoIsSet(boolean value) {
    if (!value) {
      this.orderNo = null;
    }
  }

  public int getFlag() {
    return this.flag;
  }

  public RecoverOrderIndex setFlag(int flag) {
    this.flag = flag;
    setFlagIsSet(true);
    return this;
  }

  public void unsetFlag() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __FLAG_ISSET_ID);
  }

  /** Returns true if field flag is set (has been assigned a value) and false otherwise */
  public boolean isSetFlag() {
    return EncodingUtils.testBit(__isset_bitfield, __FLAG_ISSET_ID);
  }

  public void setFlagIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __FLAG_ISSET_ID, value);
  }

  public String getCreateTime() {
    return this.createTime;
  }

  public RecoverOrderIndex setCreateTime(String createTime) {
    this.createTime = createTime;
    return this;
  }

  public void unsetCreateTime() {
    this.createTime = null;
  }

  /** Returns true if field createTime is set (has been assigned a value) and false otherwise */
  public boolean isSetCreateTime() {
    return this.createTime != null;
  }

  public void setCreateTimeIsSet(boolean value) {
    if (!value) {
      this.createTime = null;
    }
  }

  public String getLastUpdateTime() {
    return this.lastUpdateTime;
  }

  public RecoverOrderIndex setLastUpdateTime(String lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
    return this;
  }

  public void unsetLastUpdateTime() {
    this.lastUpdateTime = null;
  }

  /** Returns true if field lastUpdateTime is set (has been assigned a value) and false otherwise */
  public boolean isSetLastUpdateTime() {
    return this.lastUpdateTime != null;
  }

  public void setLastUpdateTimeIsSet(boolean value) {
    if (!value) {
      this.lastUpdateTime = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case DB_NAME:
      if (value == null) {
        unsetDbName();
      } else {
        setDbName((String)value);
      }
      break;

    case ORDER_NO:
      if (value == null) {
        unsetOrderNo();
      } else {
        setOrderNo((String)value);
      }
      break;

    case FLAG:
      if (value == null) {
        unsetFlag();
      } else {
        setFlag((Integer)value);
      }
      break;

    case CREATE_TIME:
      if (value == null) {
        unsetCreateTime();
      } else {
        setCreateTime((String)value);
      }
      break;

    case LAST_UPDATE_TIME:
      if (value == null) {
        unsetLastUpdateTime();
      } else {
        setLastUpdateTime((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case DB_NAME:
      return getDbName();

    case ORDER_NO:
      return getOrderNo();

    case FLAG:
      return Integer.valueOf(getFlag());

    case CREATE_TIME:
      return getCreateTime();

    case LAST_UPDATE_TIME:
      return getLastUpdateTime();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case DB_NAME:
      return isSetDbName();
    case ORDER_NO:
      return isSetOrderNo();
    case FLAG:
      return isSetFlag();
    case CREATE_TIME:
      return isSetCreateTime();
    case LAST_UPDATE_TIME:
      return isSetLastUpdateTime();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof RecoverOrderIndex)
      return this.equals((RecoverOrderIndex)that);
    return false;
  }

  public boolean equals(RecoverOrderIndex that) {
    if (that == null)
      return false;

    boolean this_present_dbName = true && this.isSetDbName();
    boolean that_present_dbName = true && that.isSetDbName();
    if (this_present_dbName || that_present_dbName) {
      if (!(this_present_dbName && that_present_dbName))
        return false;
      if (!this.dbName.equals(that.dbName))
        return false;
    }

    boolean this_present_orderNo = true && this.isSetOrderNo();
    boolean that_present_orderNo = true && that.isSetOrderNo();
    if (this_present_orderNo || that_present_orderNo) {
      if (!(this_present_orderNo && that_present_orderNo))
        return false;
      if (!this.orderNo.equals(that.orderNo))
        return false;
    }

    boolean this_present_flag = true;
    boolean that_present_flag = true;
    if (this_present_flag || that_present_flag) {
      if (!(this_present_flag && that_present_flag))
        return false;
      if (this.flag != that.flag)
        return false;
    }

    boolean this_present_createTime = true && this.isSetCreateTime();
    boolean that_present_createTime = true && that.isSetCreateTime();
    if (this_present_createTime || that_present_createTime) {
      if (!(this_present_createTime && that_present_createTime))
        return false;
      if (!this.createTime.equals(that.createTime))
        return false;
    }

    boolean this_present_lastUpdateTime = true && this.isSetLastUpdateTime();
    boolean that_present_lastUpdateTime = true && that.isSetLastUpdateTime();
    if (this_present_lastUpdateTime || that_present_lastUpdateTime) {
      if (!(this_present_lastUpdateTime && that_present_lastUpdateTime))
        return false;
      if (!this.lastUpdateTime.equals(that.lastUpdateTime))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(RecoverOrderIndex other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetDbName()).compareTo(other.isSetDbName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetDbName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.dbName, other.dbName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetOrderNo()).compareTo(other.isSetOrderNo());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetOrderNo()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.orderNo, other.orderNo);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetFlag()).compareTo(other.isSetFlag());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetFlag()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.flag, other.flag);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetCreateTime()).compareTo(other.isSetCreateTime());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCreateTime()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.createTime, other.createTime);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetLastUpdateTime()).compareTo(other.isSetLastUpdateTime());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLastUpdateTime()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.lastUpdateTime, other.lastUpdateTime);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("RecoverOrderIndex(");
    boolean first = true;

    sb.append("dbName:");
    if (this.dbName == null) {
      sb.append("null");
    } else {
      sb.append(this.dbName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("orderNo:");
    if (this.orderNo == null) {
      sb.append("null");
    } else {
      sb.append(this.orderNo);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("flag:");
    sb.append(this.flag);
    first = false;
    if (!first) sb.append(", ");
    sb.append("createTime:");
    if (this.createTime == null) {
      sb.append("null");
    } else {
      sb.append(this.createTime);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("lastUpdateTime:");
    if (this.lastUpdateTime == null) {
      sb.append("null");
    } else {
      sb.append(this.lastUpdateTime);
    }
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

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class RecoverOrderIndexStandardSchemeFactory implements SchemeFactory {
    public RecoverOrderIndexStandardScheme getScheme() {
      return new RecoverOrderIndexStandardScheme();
    }
  }

  private static class RecoverOrderIndexStandardScheme extends StandardScheme<RecoverOrderIndex> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, RecoverOrderIndex struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // DB_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.dbName = iprot.readString();
              struct.setDbNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // ORDER_NO
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.orderNo = iprot.readString();
              struct.setOrderNoIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // FLAG
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.flag = iprot.readI32();
              struct.setFlagIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // CREATE_TIME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.createTime = iprot.readString();
              struct.setCreateTimeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // LAST_UPDATE_TIME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.lastUpdateTime = iprot.readString();
              struct.setLastUpdateTimeIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, RecoverOrderIndex struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.dbName != null) {
        oprot.writeFieldBegin(DB_NAME_FIELD_DESC);
        oprot.writeString(struct.dbName);
        oprot.writeFieldEnd();
      }
      if (struct.orderNo != null) {
        oprot.writeFieldBegin(ORDER_NO_FIELD_DESC);
        oprot.writeString(struct.orderNo);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(FLAG_FIELD_DESC);
      oprot.writeI32(struct.flag);
      oprot.writeFieldEnd();
      if (struct.createTime != null) {
        oprot.writeFieldBegin(CREATE_TIME_FIELD_DESC);
        oprot.writeString(struct.createTime);
        oprot.writeFieldEnd();
      }
      if (struct.lastUpdateTime != null) {
        oprot.writeFieldBegin(LAST_UPDATE_TIME_FIELD_DESC);
        oprot.writeString(struct.lastUpdateTime);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class RecoverOrderIndexTupleSchemeFactory implements SchemeFactory {
    public RecoverOrderIndexTupleScheme getScheme() {
      return new RecoverOrderIndexTupleScheme();
    }
  }

  private static class RecoverOrderIndexTupleScheme extends TupleScheme<RecoverOrderIndex> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, RecoverOrderIndex struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetDbName()) {
        optionals.set(0);
      }
      if (struct.isSetOrderNo()) {
        optionals.set(1);
      }
      if (struct.isSetFlag()) {
        optionals.set(2);
      }
      if (struct.isSetCreateTime()) {
        optionals.set(3);
      }
      if (struct.isSetLastUpdateTime()) {
        optionals.set(4);
      }
      oprot.writeBitSet(optionals, 5);
      if (struct.isSetDbName()) {
        oprot.writeString(struct.dbName);
      }
      if (struct.isSetOrderNo()) {
        oprot.writeString(struct.orderNo);
      }
      if (struct.isSetFlag()) {
        oprot.writeI32(struct.flag);
      }
      if (struct.isSetCreateTime()) {
        oprot.writeString(struct.createTime);
      }
      if (struct.isSetLastUpdateTime()) {
        oprot.writeString(struct.lastUpdateTime);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, RecoverOrderIndex struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(5);
      if (incoming.get(0)) {
        struct.dbName = iprot.readString();
        struct.setDbNameIsSet(true);
      }
      if (incoming.get(1)) {
        struct.orderNo = iprot.readString();
        struct.setOrderNoIsSet(true);
      }
      if (incoming.get(2)) {
        struct.flag = iprot.readI32();
        struct.setFlagIsSet(true);
      }
      if (incoming.get(3)) {
        struct.createTime = iprot.readString();
        struct.setCreateTimeIsSet(true);
      }
      if (incoming.get(4)) {
        struct.lastUpdateTime = iprot.readString();
        struct.setLastUpdateTimeIsSet(true);
      }
    }
  }

}

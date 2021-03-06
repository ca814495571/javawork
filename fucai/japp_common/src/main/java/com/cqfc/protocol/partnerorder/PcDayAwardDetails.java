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

public class PcDayAwardDetails implements org.apache.thrift.TBase<PcDayAwardDetails, PcDayAwardDetails._Fields>, java.io.Serializable, Cloneable, Comparable<PcDayAwardDetails> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("PcDayAwardDetails");

  private static final org.apache.thrift.protocol.TField DAY_AWARD_DETAILS_FIELD_DESC = new org.apache.thrift.protocol.TField("dayAwardDetails", org.apache.thrift.protocol.TType.LIST, (short)1);
  private static final org.apache.thrift.protocol.TField TOTAL_NUM_FIELD_DESC = new org.apache.thrift.protocol.TField("totalNum", org.apache.thrift.protocol.TType.I32, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new PcDayAwardDetailsStandardSchemeFactory());
    schemes.put(TupleScheme.class, new PcDayAwardDetailsTupleSchemeFactory());
  }

  public List<DailyAwardCount> dayAwardDetails; // required
  public int totalNum; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    DAY_AWARD_DETAILS((short)1, "dayAwardDetails"),
    TOTAL_NUM((short)2, "totalNum");

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
        case 1: // DAY_AWARD_DETAILS
          return DAY_AWARD_DETAILS;
        case 2: // TOTAL_NUM
          return TOTAL_NUM;
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
  private static final int __TOTALNUM_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.DAY_AWARD_DETAILS, new org.apache.thrift.meta_data.FieldMetaData("dayAwardDetails", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, DailyAwardCount.class))));
    tmpMap.put(_Fields.TOTAL_NUM, new org.apache.thrift.meta_data.FieldMetaData("totalNum", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(PcDayAwardDetails.class, metaDataMap);
  }

  public PcDayAwardDetails() {
  }

  public PcDayAwardDetails(
    List<DailyAwardCount> dayAwardDetails,
    int totalNum)
  {
    this();
    this.dayAwardDetails = dayAwardDetails;
    this.totalNum = totalNum;
    setTotalNumIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public PcDayAwardDetails(PcDayAwardDetails other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetDayAwardDetails()) {
      List<DailyAwardCount> __this__dayAwardDetails = new ArrayList<DailyAwardCount>(other.dayAwardDetails.size());
      for (DailyAwardCount other_element : other.dayAwardDetails) {
        __this__dayAwardDetails.add(new DailyAwardCount(other_element));
      }
      this.dayAwardDetails = __this__dayAwardDetails;
    }
    this.totalNum = other.totalNum;
  }

  public PcDayAwardDetails deepCopy() {
    return new PcDayAwardDetails(this);
  }

  @Override
  public void clear() {
    this.dayAwardDetails = null;
    setTotalNumIsSet(false);
    this.totalNum = 0;
  }

  public int getDayAwardDetailsSize() {
    return (this.dayAwardDetails == null) ? 0 : this.dayAwardDetails.size();
  }

  public java.util.Iterator<DailyAwardCount> getDayAwardDetailsIterator() {
    return (this.dayAwardDetails == null) ? null : this.dayAwardDetails.iterator();
  }

  public void addToDayAwardDetails(DailyAwardCount elem) {
    if (this.dayAwardDetails == null) {
      this.dayAwardDetails = new ArrayList<DailyAwardCount>();
    }
    this.dayAwardDetails.add(elem);
  }

  public List<DailyAwardCount> getDayAwardDetails() {
    return this.dayAwardDetails;
  }

  public PcDayAwardDetails setDayAwardDetails(List<DailyAwardCount> dayAwardDetails) {
    this.dayAwardDetails = dayAwardDetails;
    return this;
  }

  public void unsetDayAwardDetails() {
    this.dayAwardDetails = null;
  }

  /** Returns true if field dayAwardDetails is set (has been assigned a value) and false otherwise */
  public boolean isSetDayAwardDetails() {
    return this.dayAwardDetails != null;
  }

  public void setDayAwardDetailsIsSet(boolean value) {
    if (!value) {
      this.dayAwardDetails = null;
    }
  }

  public int getTotalNum() {
    return this.totalNum;
  }

  public PcDayAwardDetails setTotalNum(int totalNum) {
    this.totalNum = totalNum;
    setTotalNumIsSet(true);
    return this;
  }

  public void unsetTotalNum() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __TOTALNUM_ISSET_ID);
  }

  /** Returns true if field totalNum is set (has been assigned a value) and false otherwise */
  public boolean isSetTotalNum() {
    return EncodingUtils.testBit(__isset_bitfield, __TOTALNUM_ISSET_ID);
  }

  public void setTotalNumIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __TOTALNUM_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case DAY_AWARD_DETAILS:
      if (value == null) {
        unsetDayAwardDetails();
      } else {
        setDayAwardDetails((List<DailyAwardCount>)value);
      }
      break;

    case TOTAL_NUM:
      if (value == null) {
        unsetTotalNum();
      } else {
        setTotalNum((Integer)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case DAY_AWARD_DETAILS:
      return getDayAwardDetails();

    case TOTAL_NUM:
      return Integer.valueOf(getTotalNum());

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case DAY_AWARD_DETAILS:
      return isSetDayAwardDetails();
    case TOTAL_NUM:
      return isSetTotalNum();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof PcDayAwardDetails)
      return this.equals((PcDayAwardDetails)that);
    return false;
  }

  public boolean equals(PcDayAwardDetails that) {
    if (that == null)
      return false;

    boolean this_present_dayAwardDetails = true && this.isSetDayAwardDetails();
    boolean that_present_dayAwardDetails = true && that.isSetDayAwardDetails();
    if (this_present_dayAwardDetails || that_present_dayAwardDetails) {
      if (!(this_present_dayAwardDetails && that_present_dayAwardDetails))
        return false;
      if (!this.dayAwardDetails.equals(that.dayAwardDetails))
        return false;
    }

    boolean this_present_totalNum = true;
    boolean that_present_totalNum = true;
    if (this_present_totalNum || that_present_totalNum) {
      if (!(this_present_totalNum && that_present_totalNum))
        return false;
      if (this.totalNum != that.totalNum)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(PcDayAwardDetails other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetDayAwardDetails()).compareTo(other.isSetDayAwardDetails());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetDayAwardDetails()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.dayAwardDetails, other.dayAwardDetails);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetTotalNum()).compareTo(other.isSetTotalNum());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTotalNum()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.totalNum, other.totalNum);
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
    StringBuilder sb = new StringBuilder("PcDayAwardDetails(");
    boolean first = true;

    sb.append("dayAwardDetails:");
    if (this.dayAwardDetails == null) {
      sb.append("null");
    } else {
      sb.append(this.dayAwardDetails);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("totalNum:");
    sb.append(this.totalNum);
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

  private static class PcDayAwardDetailsStandardSchemeFactory implements SchemeFactory {
    public PcDayAwardDetailsStandardScheme getScheme() {
      return new PcDayAwardDetailsStandardScheme();
    }
  }

  private static class PcDayAwardDetailsStandardScheme extends StandardScheme<PcDayAwardDetails> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, PcDayAwardDetails struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // DAY_AWARD_DETAILS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list40 = iprot.readListBegin();
                struct.dayAwardDetails = new ArrayList<DailyAwardCount>(_list40.size);
                for (int _i41 = 0; _i41 < _list40.size; ++_i41)
                {
                  DailyAwardCount _elem42;
                  _elem42 = new DailyAwardCount();
                  _elem42.read(iprot);
                  struct.dayAwardDetails.add(_elem42);
                }
                iprot.readListEnd();
              }
              struct.setDayAwardDetailsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // TOTAL_NUM
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.totalNum = iprot.readI32();
              struct.setTotalNumIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, PcDayAwardDetails struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.dayAwardDetails != null) {
        oprot.writeFieldBegin(DAY_AWARD_DETAILS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.dayAwardDetails.size()));
          for (DailyAwardCount _iter43 : struct.dayAwardDetails)
          {
            _iter43.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(TOTAL_NUM_FIELD_DESC);
      oprot.writeI32(struct.totalNum);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class PcDayAwardDetailsTupleSchemeFactory implements SchemeFactory {
    public PcDayAwardDetailsTupleScheme getScheme() {
      return new PcDayAwardDetailsTupleScheme();
    }
  }

  private static class PcDayAwardDetailsTupleScheme extends TupleScheme<PcDayAwardDetails> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, PcDayAwardDetails struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetDayAwardDetails()) {
        optionals.set(0);
      }
      if (struct.isSetTotalNum()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetDayAwardDetails()) {
        {
          oprot.writeI32(struct.dayAwardDetails.size());
          for (DailyAwardCount _iter44 : struct.dayAwardDetails)
          {
            _iter44.write(oprot);
          }
        }
      }
      if (struct.isSetTotalNum()) {
        oprot.writeI32(struct.totalNum);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, PcDayAwardDetails struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list45 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.dayAwardDetails = new ArrayList<DailyAwardCount>(_list45.size);
          for (int _i46 = 0; _i46 < _list45.size; ++_i46)
          {
            DailyAwardCount _elem47;
            _elem47 = new DailyAwardCount();
            _elem47.read(iprot);
            struct.dayAwardDetails.add(_elem47);
          }
        }
        struct.setDayAwardDetailsIsSet(true);
      }
      if (incoming.get(1)) {
        struct.totalNum = iprot.readI32();
        struct.setTotalNumIsSet(true);
      }
    }
  }

}


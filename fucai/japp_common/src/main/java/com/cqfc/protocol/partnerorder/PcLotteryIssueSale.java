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

public class PcLotteryIssueSale implements org.apache.thrift.TBase<PcLotteryIssueSale, PcLotteryIssueSale._Fields>, java.io.Serializable, Cloneable, Comparable<PcLotteryIssueSale> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("PcLotteryIssueSale");

  private static final org.apache.thrift.protocol.TField LOTTERY_ISSUE_SALE_FIELD_DESC = new org.apache.thrift.protocol.TField("lotteryIssueSale", org.apache.thrift.protocol.TType.LIST, (short)1);
  private static final org.apache.thrift.protocol.TField TOTAL_NUM_FIELD_DESC = new org.apache.thrift.protocol.TField("totalNum", org.apache.thrift.protocol.TType.I32, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new PcLotteryIssueSaleStandardSchemeFactory());
    schemes.put(TupleScheme.class, new PcLotteryIssueSaleTupleSchemeFactory());
  }

  public List<LotteryIssueSale> lotteryIssueSale; // required
  public int totalNum; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    LOTTERY_ISSUE_SALE((short)1, "lotteryIssueSale"),
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
        case 1: // LOTTERY_ISSUE_SALE
          return LOTTERY_ISSUE_SALE;
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
    tmpMap.put(_Fields.LOTTERY_ISSUE_SALE, new org.apache.thrift.meta_data.FieldMetaData("lotteryIssueSale", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, LotteryIssueSale.class))));
    tmpMap.put(_Fields.TOTAL_NUM, new org.apache.thrift.meta_data.FieldMetaData("totalNum", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(PcLotteryIssueSale.class, metaDataMap);
  }

  public PcLotteryIssueSale() {
  }

  public PcLotteryIssueSale(
    List<LotteryIssueSale> lotteryIssueSale,
    int totalNum)
  {
    this();
    this.lotteryIssueSale = lotteryIssueSale;
    this.totalNum = totalNum;
    setTotalNumIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public PcLotteryIssueSale(PcLotteryIssueSale other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetLotteryIssueSale()) {
      List<LotteryIssueSale> __this__lotteryIssueSale = new ArrayList<LotteryIssueSale>(other.lotteryIssueSale.size());
      for (LotteryIssueSale other_element : other.lotteryIssueSale) {
        __this__lotteryIssueSale.add(new LotteryIssueSale(other_element));
      }
      this.lotteryIssueSale = __this__lotteryIssueSale;
    }
    this.totalNum = other.totalNum;
  }

  public PcLotteryIssueSale deepCopy() {
    return new PcLotteryIssueSale(this);
  }

  @Override
  public void clear() {
    this.lotteryIssueSale = null;
    setTotalNumIsSet(false);
    this.totalNum = 0;
  }

  public int getLotteryIssueSaleSize() {
    return (this.lotteryIssueSale == null) ? 0 : this.lotteryIssueSale.size();
  }

  public java.util.Iterator<LotteryIssueSale> getLotteryIssueSaleIterator() {
    return (this.lotteryIssueSale == null) ? null : this.lotteryIssueSale.iterator();
  }

  public void addToLotteryIssueSale(LotteryIssueSale elem) {
    if (this.lotteryIssueSale == null) {
      this.lotteryIssueSale = new ArrayList<LotteryIssueSale>();
    }
    this.lotteryIssueSale.add(elem);
  }

  public List<LotteryIssueSale> getLotteryIssueSale() {
    return this.lotteryIssueSale;
  }

  public PcLotteryIssueSale setLotteryIssueSale(List<LotteryIssueSale> lotteryIssueSale) {
    this.lotteryIssueSale = lotteryIssueSale;
    return this;
  }

  public void unsetLotteryIssueSale() {
    this.lotteryIssueSale = null;
  }

  /** Returns true if field lotteryIssueSale is set (has been assigned a value) and false otherwise */
  public boolean isSetLotteryIssueSale() {
    return this.lotteryIssueSale != null;
  }

  public void setLotteryIssueSaleIsSet(boolean value) {
    if (!value) {
      this.lotteryIssueSale = null;
    }
  }

  public int getTotalNum() {
    return this.totalNum;
  }

  public PcLotteryIssueSale setTotalNum(int totalNum) {
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
    case LOTTERY_ISSUE_SALE:
      if (value == null) {
        unsetLotteryIssueSale();
      } else {
        setLotteryIssueSale((List<LotteryIssueSale>)value);
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
    case LOTTERY_ISSUE_SALE:
      return getLotteryIssueSale();

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
    case LOTTERY_ISSUE_SALE:
      return isSetLotteryIssueSale();
    case TOTAL_NUM:
      return isSetTotalNum();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof PcLotteryIssueSale)
      return this.equals((PcLotteryIssueSale)that);
    return false;
  }

  public boolean equals(PcLotteryIssueSale that) {
    if (that == null)
      return false;

    boolean this_present_lotteryIssueSale = true && this.isSetLotteryIssueSale();
    boolean that_present_lotteryIssueSale = true && that.isSetLotteryIssueSale();
    if (this_present_lotteryIssueSale || that_present_lotteryIssueSale) {
      if (!(this_present_lotteryIssueSale && that_present_lotteryIssueSale))
        return false;
      if (!this.lotteryIssueSale.equals(that.lotteryIssueSale))
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
  public int compareTo(PcLotteryIssueSale other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetLotteryIssueSale()).compareTo(other.isSetLotteryIssueSale());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLotteryIssueSale()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.lotteryIssueSale, other.lotteryIssueSale);
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
    StringBuilder sb = new StringBuilder("PcLotteryIssueSale(");
    boolean first = true;

    sb.append("lotteryIssueSale:");
    if (this.lotteryIssueSale == null) {
      sb.append("null");
    } else {
      sb.append(this.lotteryIssueSale);
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

  private static class PcLotteryIssueSaleStandardSchemeFactory implements SchemeFactory {
    public PcLotteryIssueSaleStandardScheme getScheme() {
      return new PcLotteryIssueSaleStandardScheme();
    }
  }

  private static class PcLotteryIssueSaleStandardScheme extends StandardScheme<PcLotteryIssueSale> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, PcLotteryIssueSale struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // LOTTERY_ISSUE_SALE
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list8 = iprot.readListBegin();
                struct.lotteryIssueSale = new ArrayList<LotteryIssueSale>(_list8.size);
                for (int _i9 = 0; _i9 < _list8.size; ++_i9)
                {
                  LotteryIssueSale _elem10;
                  _elem10 = new LotteryIssueSale();
                  _elem10.read(iprot);
                  struct.lotteryIssueSale.add(_elem10);
                }
                iprot.readListEnd();
              }
              struct.setLotteryIssueSaleIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, PcLotteryIssueSale struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.lotteryIssueSale != null) {
        oprot.writeFieldBegin(LOTTERY_ISSUE_SALE_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.lotteryIssueSale.size()));
          for (LotteryIssueSale _iter11 : struct.lotteryIssueSale)
          {
            _iter11.write(oprot);
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

  private static class PcLotteryIssueSaleTupleSchemeFactory implements SchemeFactory {
    public PcLotteryIssueSaleTupleScheme getScheme() {
      return new PcLotteryIssueSaleTupleScheme();
    }
  }

  private static class PcLotteryIssueSaleTupleScheme extends TupleScheme<PcLotteryIssueSale> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, PcLotteryIssueSale struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetLotteryIssueSale()) {
        optionals.set(0);
      }
      if (struct.isSetTotalNum()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetLotteryIssueSale()) {
        {
          oprot.writeI32(struct.lotteryIssueSale.size());
          for (LotteryIssueSale _iter12 : struct.lotteryIssueSale)
          {
            _iter12.write(oprot);
          }
        }
      }
      if (struct.isSetTotalNum()) {
        oprot.writeI32(struct.totalNum);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, PcLotteryIssueSale struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list13 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.lotteryIssueSale = new ArrayList<LotteryIssueSale>(_list13.size);
          for (int _i14 = 0; _i14 < _list13.size; ++_i14)
          {
            LotteryIssueSale _elem15;
            _elem15 = new LotteryIssueSale();
            _elem15.read(iprot);
            struct.lotteryIssueSale.add(_elem15);
          }
        }
        struct.setLotteryIssueSaleIsSet(true);
      }
      if (incoming.get(1)) {
        struct.totalNum = iprot.readI32();
        struct.setTotalNumIsSet(true);
      }
    }
  }

}


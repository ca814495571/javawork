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

public class PcPartnerOrder implements org.apache.thrift.TBase<PcPartnerOrder, PcPartnerOrder._Fields>, java.io.Serializable, Cloneable, Comparable<PcPartnerOrder> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("PcPartnerOrder");

  private static final org.apache.thrift.protocol.TField PARTNER_ORDERS_FIELD_DESC = new org.apache.thrift.protocol.TField("partnerOrders", org.apache.thrift.protocol.TType.LIST, (short)1);
  private static final org.apache.thrift.protocol.TField TOTAL_NUM_FIELD_DESC = new org.apache.thrift.protocol.TField("totalNum", org.apache.thrift.protocol.TType.I32, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new PcPartnerOrderStandardSchemeFactory());
    schemes.put(TupleScheme.class, new PcPartnerOrderTupleSchemeFactory());
  }

  public List<Order> partnerOrders; // required
  public int totalNum; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    PARTNER_ORDERS((short)1, "partnerOrders"),
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
        case 1: // PARTNER_ORDERS
          return PARTNER_ORDERS;
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
    tmpMap.put(_Fields.PARTNER_ORDERS, new org.apache.thrift.meta_data.FieldMetaData("partnerOrders", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Order.class))));
    tmpMap.put(_Fields.TOTAL_NUM, new org.apache.thrift.meta_data.FieldMetaData("totalNum", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(PcPartnerOrder.class, metaDataMap);
  }

  public PcPartnerOrder() {
  }

  public PcPartnerOrder(
    List<Order> partnerOrders,
    int totalNum)
  {
    this();
    this.partnerOrders = partnerOrders;
    this.totalNum = totalNum;
    setTotalNumIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public PcPartnerOrder(PcPartnerOrder other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetPartnerOrders()) {
      List<Order> __this__partnerOrders = new ArrayList<Order>(other.partnerOrders.size());
      for (Order other_element : other.partnerOrders) {
        __this__partnerOrders.add(new Order(other_element));
      }
      this.partnerOrders = __this__partnerOrders;
    }
    this.totalNum = other.totalNum;
  }

  public PcPartnerOrder deepCopy() {
    return new PcPartnerOrder(this);
  }

  @Override
  public void clear() {
    this.partnerOrders = null;
    setTotalNumIsSet(false);
    this.totalNum = 0;
  }

  public int getPartnerOrdersSize() {
    return (this.partnerOrders == null) ? 0 : this.partnerOrders.size();
  }

  public java.util.Iterator<Order> getPartnerOrdersIterator() {
    return (this.partnerOrders == null) ? null : this.partnerOrders.iterator();
  }

  public void addToPartnerOrders(Order elem) {
    if (this.partnerOrders == null) {
      this.partnerOrders = new ArrayList<Order>();
    }
    this.partnerOrders.add(elem);
  }

  public List<Order> getPartnerOrders() {
    return this.partnerOrders;
  }

  public PcPartnerOrder setPartnerOrders(List<Order> partnerOrders) {
    this.partnerOrders = partnerOrders;
    return this;
  }

  public void unsetPartnerOrders() {
    this.partnerOrders = null;
  }

  /** Returns true if field partnerOrders is set (has been assigned a value) and false otherwise */
  public boolean isSetPartnerOrders() {
    return this.partnerOrders != null;
  }

  public void setPartnerOrdersIsSet(boolean value) {
    if (!value) {
      this.partnerOrders = null;
    }
  }

  public int getTotalNum() {
    return this.totalNum;
  }

  public PcPartnerOrder setTotalNum(int totalNum) {
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
    case PARTNER_ORDERS:
      if (value == null) {
        unsetPartnerOrders();
      } else {
        setPartnerOrders((List<Order>)value);
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
    case PARTNER_ORDERS:
      return getPartnerOrders();

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
    case PARTNER_ORDERS:
      return isSetPartnerOrders();
    case TOTAL_NUM:
      return isSetTotalNum();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof PcPartnerOrder)
      return this.equals((PcPartnerOrder)that);
    return false;
  }

  public boolean equals(PcPartnerOrder that) {
    if (that == null)
      return false;

    boolean this_present_partnerOrders = true && this.isSetPartnerOrders();
    boolean that_present_partnerOrders = true && that.isSetPartnerOrders();
    if (this_present_partnerOrders || that_present_partnerOrders) {
      if (!(this_present_partnerOrders && that_present_partnerOrders))
        return false;
      if (!this.partnerOrders.equals(that.partnerOrders))
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
  public int compareTo(PcPartnerOrder other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetPartnerOrders()).compareTo(other.isSetPartnerOrders());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPartnerOrders()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.partnerOrders, other.partnerOrders);
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
    StringBuilder sb = new StringBuilder("PcPartnerOrder(");
    boolean first = true;

    sb.append("partnerOrders:");
    if (this.partnerOrders == null) {
      sb.append("null");
    } else {
      sb.append(this.partnerOrders);
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

  private static class PcPartnerOrderStandardSchemeFactory implements SchemeFactory {
    public PcPartnerOrderStandardScheme getScheme() {
      return new PcPartnerOrderStandardScheme();
    }
  }

  private static class PcPartnerOrderStandardScheme extends StandardScheme<PcPartnerOrder> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, PcPartnerOrder struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // PARTNER_ORDERS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list16 = iprot.readListBegin();
                struct.partnerOrders = new ArrayList<Order>(_list16.size);
                for (int _i17 = 0; _i17 < _list16.size; ++_i17)
                {
                  Order _elem18;
                  _elem18 = new Order();
                  _elem18.read(iprot);
                  struct.partnerOrders.add(_elem18);
                }
                iprot.readListEnd();
              }
              struct.setPartnerOrdersIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, PcPartnerOrder struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.partnerOrders != null) {
        oprot.writeFieldBegin(PARTNER_ORDERS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.partnerOrders.size()));
          for (Order _iter19 : struct.partnerOrders)
          {
            _iter19.write(oprot);
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

  private static class PcPartnerOrderTupleSchemeFactory implements SchemeFactory {
    public PcPartnerOrderTupleScheme getScheme() {
      return new PcPartnerOrderTupleScheme();
    }
  }

  private static class PcPartnerOrderTupleScheme extends TupleScheme<PcPartnerOrder> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, PcPartnerOrder struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetPartnerOrders()) {
        optionals.set(0);
      }
      if (struct.isSetTotalNum()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetPartnerOrders()) {
        {
          oprot.writeI32(struct.partnerOrders.size());
          for (Order _iter20 : struct.partnerOrders)
          {
            _iter20.write(oprot);
          }
        }
      }
      if (struct.isSetTotalNum()) {
        oprot.writeI32(struct.totalNum);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, PcPartnerOrder struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list21 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.partnerOrders = new ArrayList<Order>(_list21.size);
          for (int _i22 = 0; _i22 < _list21.size; ++_i22)
          {
            Order _elem23;
            _elem23 = new Order();
            _elem23.read(iprot);
            struct.partnerOrders.add(_elem23);
          }
        }
        struct.setPartnerOrdersIsSet(true);
      }
      if (incoming.get(1)) {
        struct.totalNum = iprot.readI32();
        struct.setTotalNumIsSet(true);
      }
    }
  }

}


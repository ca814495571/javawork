/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.cqfc.UserInfo.protocol;

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

public class Message implements org.apache.thrift.TBase<Message, Message._Fields>, java.io.Serializable, Cloneable, Comparable<Message> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Message");

  private static final org.apache.thrift.protocol.TField TRANS_CODE_FIELD_DESC = new org.apache.thrift.protocol.TField("transCode", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField MSG_FIELD_DESC = new org.apache.thrift.protocol.TField("msg", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField PARTNER_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("partnerID", org.apache.thrift.protocol.TType.I32, (short)3);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new MessageStandardSchemeFactory());
    schemes.put(TupleScheme.class, new MessageTupleSchemeFactory());
  }

  public int transCode; // required
  public String msg; // required
  public int partnerID; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    TRANS_CODE((short)1, "transCode"),
    MSG((short)2, "msg"),
    PARTNER_ID((short)3, "partnerID");

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
        case 1: // TRANS_CODE
          return TRANS_CODE;
        case 2: // MSG
          return MSG;
        case 3: // PARTNER_ID
          return PARTNER_ID;
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
  private static final int __TRANSCODE_ISSET_ID = 0;
  private static final int __PARTNERID_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.TRANS_CODE, new org.apache.thrift.meta_data.FieldMetaData("transCode", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.MSG, new org.apache.thrift.meta_data.FieldMetaData("msg", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PARTNER_ID, new org.apache.thrift.meta_data.FieldMetaData("partnerID", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Message.class, metaDataMap);
  }

  public Message() {
  }

  public Message(
    int transCode,
    String msg,
    int partnerID)
  {
    this();
    this.transCode = transCode;
    setTransCodeIsSet(true);
    this.msg = msg;
    this.partnerID = partnerID;
    setPartnerIDIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Message(Message other) {
    __isset_bitfield = other.__isset_bitfield;
    this.transCode = other.transCode;
    if (other.isSetMsg()) {
      this.msg = other.msg;
    }
    this.partnerID = other.partnerID;
  }

  public Message deepCopy() {
    return new Message(this);
  }

  @Override
  public void clear() {
    setTransCodeIsSet(false);
    this.transCode = 0;
    this.msg = null;
    setPartnerIDIsSet(false);
    this.partnerID = 0;
  }

  public int getTransCode() {
    return this.transCode;
  }

  public Message setTransCode(int transCode) {
    this.transCode = transCode;
    setTransCodeIsSet(true);
    return this;
  }

  public void unsetTransCode() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __TRANSCODE_ISSET_ID);
  }

  /** Returns true if field transCode is set (has been assigned a value) and false otherwise */
  public boolean isSetTransCode() {
    return EncodingUtils.testBit(__isset_bitfield, __TRANSCODE_ISSET_ID);
  }

  public void setTransCodeIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __TRANSCODE_ISSET_ID, value);
  }

  public String getMsg() {
    return this.msg;
  }

  public Message setMsg(String msg) {
    this.msg = msg;
    return this;
  }

  public void unsetMsg() {
    this.msg = null;
  }

  /** Returns true if field msg is set (has been assigned a value) and false otherwise */
  public boolean isSetMsg() {
    return this.msg != null;
  }

  public void setMsgIsSet(boolean value) {
    if (!value) {
      this.msg = null;
    }
  }

  public int getPartnerID() {
    return this.partnerID;
  }

  public Message setPartnerID(int partnerID) {
    this.partnerID = partnerID;
    setPartnerIDIsSet(true);
    return this;
  }

  public void unsetPartnerID() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __PARTNERID_ISSET_ID);
  }

  /** Returns true if field partnerID is set (has been assigned a value) and false otherwise */
  public boolean isSetPartnerID() {
    return EncodingUtils.testBit(__isset_bitfield, __PARTNERID_ISSET_ID);
  }

  public void setPartnerIDIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __PARTNERID_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case TRANS_CODE:
      if (value == null) {
        unsetTransCode();
      } else {
        setTransCode((Integer)value);
      }
      break;

    case MSG:
      if (value == null) {
        unsetMsg();
      } else {
        setMsg((String)value);
      }
      break;

    case PARTNER_ID:
      if (value == null) {
        unsetPartnerID();
      } else {
        setPartnerID((Integer)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case TRANS_CODE:
      return Integer.valueOf(getTransCode());

    case MSG:
      return getMsg();

    case PARTNER_ID:
      return Integer.valueOf(getPartnerID());

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case TRANS_CODE:
      return isSetTransCode();
    case MSG:
      return isSetMsg();
    case PARTNER_ID:
      return isSetPartnerID();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Message)
      return this.equals((Message)that);
    return false;
  }

  public boolean equals(Message that) {
    if (that == null)
      return false;

    boolean this_present_transCode = true;
    boolean that_present_transCode = true;
    if (this_present_transCode || that_present_transCode) {
      if (!(this_present_transCode && that_present_transCode))
        return false;
      if (this.transCode != that.transCode)
        return false;
    }

    boolean this_present_msg = true && this.isSetMsg();
    boolean that_present_msg = true && that.isSetMsg();
    if (this_present_msg || that_present_msg) {
      if (!(this_present_msg && that_present_msg))
        return false;
      if (!this.msg.equals(that.msg))
        return false;
    }

    boolean this_present_partnerID = true;
    boolean that_present_partnerID = true;
    if (this_present_partnerID || that_present_partnerID) {
      if (!(this_present_partnerID && that_present_partnerID))
        return false;
      if (this.partnerID != that.partnerID)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(Message other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetTransCode()).compareTo(other.isSetTransCode());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTransCode()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.transCode, other.transCode);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMsg()).compareTo(other.isSetMsg());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMsg()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.msg, other.msg);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPartnerID()).compareTo(other.isSetPartnerID());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPartnerID()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.partnerID, other.partnerID);
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
    StringBuilder sb = new StringBuilder("Message(");
    boolean first = true;

    sb.append("transCode:");
    sb.append(this.transCode);
    first = false;
    if (!first) sb.append(", ");
    sb.append("msg:");
    if (this.msg == null) {
      sb.append("null");
    } else {
      sb.append(this.msg);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("partnerID:");
    sb.append(this.partnerID);
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

  private static class MessageStandardSchemeFactory implements SchemeFactory {
    public MessageStandardScheme getScheme() {
      return new MessageStandardScheme();
    }
  }

  private static class MessageStandardScheme extends StandardScheme<Message> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Message struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // TRANS_CODE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.transCode = iprot.readI32();
              struct.setTransCodeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // MSG
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.msg = iprot.readString();
              struct.setMsgIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // PARTNER_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.partnerID = iprot.readI32();
              struct.setPartnerIDIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, Message struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(TRANS_CODE_FIELD_DESC);
      oprot.writeI32(struct.transCode);
      oprot.writeFieldEnd();
      if (struct.msg != null) {
        oprot.writeFieldBegin(MSG_FIELD_DESC);
        oprot.writeString(struct.msg);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(PARTNER_ID_FIELD_DESC);
      oprot.writeI32(struct.partnerID);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class MessageTupleSchemeFactory implements SchemeFactory {
    public MessageTupleScheme getScheme() {
      return new MessageTupleScheme();
    }
  }

  private static class MessageTupleScheme extends TupleScheme<Message> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Message struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetTransCode()) {
        optionals.set(0);
      }
      if (struct.isSetMsg()) {
        optionals.set(1);
      }
      if (struct.isSetPartnerID()) {
        optionals.set(2);
      }
      oprot.writeBitSet(optionals, 3);
      if (struct.isSetTransCode()) {
        oprot.writeI32(struct.transCode);
      }
      if (struct.isSetMsg()) {
        oprot.writeString(struct.msg);
      }
      if (struct.isSetPartnerID()) {
        oprot.writeI32(struct.partnerID);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Message struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(3);
      if (incoming.get(0)) {
        struct.transCode = iprot.readI32();
        struct.setTransCodeIsSet(true);
      }
      if (incoming.get(1)) {
        struct.msg = iprot.readString();
        struct.setMsgIsSet(true);
      }
      if (incoming.get(2)) {
        struct.partnerID = iprot.readI32();
        struct.setPartnerIDIsSet(true);
      }
    }
  }

}


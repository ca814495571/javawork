/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.cqfc.protocol.partner;

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

public class PartnerIpAddress implements org.apache.thrift.TBase<PartnerIpAddress, PartnerIpAddress._Fields>, java.io.Serializable, Cloneable, Comparable<PartnerIpAddress> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("PartnerIpAddress");

  private static final org.apache.thrift.protocol.TField ID_FIELD_DESC = new org.apache.thrift.protocol.TField("id", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField IP_ADDRESS_FIELD_DESC = new org.apache.thrift.protocol.TField("ipAddress", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField PARTNER_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("partnerId", org.apache.thrift.protocol.TType.STRING, (short)3);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new PartnerIpAddressStandardSchemeFactory());
    schemes.put(TupleScheme.class, new PartnerIpAddressTupleSchemeFactory());
  }

  public int id; // required
  public String ipAddress; // required
  public String partnerId; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    ID((short)1, "id"),
    IP_ADDRESS((short)2, "ipAddress"),
    PARTNER_ID((short)3, "partnerId");

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
        case 1: // ID
          return ID;
        case 2: // IP_ADDRESS
          return IP_ADDRESS;
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
  private static final int __ID_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.ID, new org.apache.thrift.meta_data.FieldMetaData("id", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.IP_ADDRESS, new org.apache.thrift.meta_data.FieldMetaData("ipAddress", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PARTNER_ID, new org.apache.thrift.meta_data.FieldMetaData("partnerId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(PartnerIpAddress.class, metaDataMap);
  }

  public PartnerIpAddress() {
  }

  public PartnerIpAddress(
    int id,
    String ipAddress,
    String partnerId)
  {
    this();
    this.id = id;
    setIdIsSet(true);
    this.ipAddress = ipAddress;
    this.partnerId = partnerId;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public PartnerIpAddress(PartnerIpAddress other) {
    __isset_bitfield = other.__isset_bitfield;
    this.id = other.id;
    if (other.isSetIpAddress()) {
      this.ipAddress = other.ipAddress;
    }
    if (other.isSetPartnerId()) {
      this.partnerId = other.partnerId;
    }
  }

  public PartnerIpAddress deepCopy() {
    return new PartnerIpAddress(this);
  }

  @Override
  public void clear() {
    setIdIsSet(false);
    this.id = 0;
    this.ipAddress = null;
    this.partnerId = null;
  }

  public int getId() {
    return this.id;
  }

  public PartnerIpAddress setId(int id) {
    this.id = id;
    setIdIsSet(true);
    return this;
  }

  public void unsetId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __ID_ISSET_ID);
  }

  /** Returns true if field id is set (has been assigned a value) and false otherwise */
  public boolean isSetId() {
    return EncodingUtils.testBit(__isset_bitfield, __ID_ISSET_ID);
  }

  public void setIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __ID_ISSET_ID, value);
  }

  public String getIpAddress() {
    return this.ipAddress;
  }

  public PartnerIpAddress setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
    return this;
  }

  public void unsetIpAddress() {
    this.ipAddress = null;
  }

  /** Returns true if field ipAddress is set (has been assigned a value) and false otherwise */
  public boolean isSetIpAddress() {
    return this.ipAddress != null;
  }

  public void setIpAddressIsSet(boolean value) {
    if (!value) {
      this.ipAddress = null;
    }
  }

  public String getPartnerId() {
    return this.partnerId;
  }

  public PartnerIpAddress setPartnerId(String partnerId) {
    this.partnerId = partnerId;
    return this;
  }

  public void unsetPartnerId() {
    this.partnerId = null;
  }

  /** Returns true if field partnerId is set (has been assigned a value) and false otherwise */
  public boolean isSetPartnerId() {
    return this.partnerId != null;
  }

  public void setPartnerIdIsSet(boolean value) {
    if (!value) {
      this.partnerId = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case ID:
      if (value == null) {
        unsetId();
      } else {
        setId((Integer)value);
      }
      break;

    case IP_ADDRESS:
      if (value == null) {
        unsetIpAddress();
      } else {
        setIpAddress((String)value);
      }
      break;

    case PARTNER_ID:
      if (value == null) {
        unsetPartnerId();
      } else {
        setPartnerId((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case ID:
      return Integer.valueOf(getId());

    case IP_ADDRESS:
      return getIpAddress();

    case PARTNER_ID:
      return getPartnerId();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case ID:
      return isSetId();
    case IP_ADDRESS:
      return isSetIpAddress();
    case PARTNER_ID:
      return isSetPartnerId();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof PartnerIpAddress)
      return this.equals((PartnerIpAddress)that);
    return false;
  }

  public boolean equals(PartnerIpAddress that) {
    if (that == null)
      return false;

    boolean this_present_id = true;
    boolean that_present_id = true;
    if (this_present_id || that_present_id) {
      if (!(this_present_id && that_present_id))
        return false;
      if (this.id != that.id)
        return false;
    }

    boolean this_present_ipAddress = true && this.isSetIpAddress();
    boolean that_present_ipAddress = true && that.isSetIpAddress();
    if (this_present_ipAddress || that_present_ipAddress) {
      if (!(this_present_ipAddress && that_present_ipAddress))
        return false;
      if (!this.ipAddress.equals(that.ipAddress))
        return false;
    }

    boolean this_present_partnerId = true && this.isSetPartnerId();
    boolean that_present_partnerId = true && that.isSetPartnerId();
    if (this_present_partnerId || that_present_partnerId) {
      if (!(this_present_partnerId && that_present_partnerId))
        return false;
      if (!this.partnerId.equals(that.partnerId))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(PartnerIpAddress other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetId()).compareTo(other.isSetId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.id, other.id);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetIpAddress()).compareTo(other.isSetIpAddress());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetIpAddress()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.ipAddress, other.ipAddress);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPartnerId()).compareTo(other.isSetPartnerId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPartnerId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.partnerId, other.partnerId);
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
    StringBuilder sb = new StringBuilder("PartnerIpAddress(");
    boolean first = true;

    sb.append("id:");
    sb.append(this.id);
    first = false;
    if (!first) sb.append(", ");
    sb.append("ipAddress:");
    if (this.ipAddress == null) {
      sb.append("null");
    } else {
      sb.append(this.ipAddress);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("partnerId:");
    if (this.partnerId == null) {
      sb.append("null");
    } else {
      sb.append(this.partnerId);
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

  private static class PartnerIpAddressStandardSchemeFactory implements SchemeFactory {
    public PartnerIpAddressStandardScheme getScheme() {
      return new PartnerIpAddressStandardScheme();
    }
  }

  private static class PartnerIpAddressStandardScheme extends StandardScheme<PartnerIpAddress> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, PartnerIpAddress struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.id = iprot.readI32();
              struct.setIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // IP_ADDRESS
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.ipAddress = iprot.readString();
              struct.setIpAddressIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // PARTNER_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.partnerId = iprot.readString();
              struct.setPartnerIdIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, PartnerIpAddress struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(ID_FIELD_DESC);
      oprot.writeI32(struct.id);
      oprot.writeFieldEnd();
      if (struct.ipAddress != null) {
        oprot.writeFieldBegin(IP_ADDRESS_FIELD_DESC);
        oprot.writeString(struct.ipAddress);
        oprot.writeFieldEnd();
      }
      if (struct.partnerId != null) {
        oprot.writeFieldBegin(PARTNER_ID_FIELD_DESC);
        oprot.writeString(struct.partnerId);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class PartnerIpAddressTupleSchemeFactory implements SchemeFactory {
    public PartnerIpAddressTupleScheme getScheme() {
      return new PartnerIpAddressTupleScheme();
    }
  }

  private static class PartnerIpAddressTupleScheme extends TupleScheme<PartnerIpAddress> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, PartnerIpAddress struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetId()) {
        optionals.set(0);
      }
      if (struct.isSetIpAddress()) {
        optionals.set(1);
      }
      if (struct.isSetPartnerId()) {
        optionals.set(2);
      }
      oprot.writeBitSet(optionals, 3);
      if (struct.isSetId()) {
        oprot.writeI32(struct.id);
      }
      if (struct.isSetIpAddress()) {
        oprot.writeString(struct.ipAddress);
      }
      if (struct.isSetPartnerId()) {
        oprot.writeString(struct.partnerId);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, PartnerIpAddress struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(3);
      if (incoming.get(0)) {
        struct.id = iprot.readI32();
        struct.setIdIsSet(true);
      }
      if (incoming.get(1)) {
        struct.ipAddress = iprot.readString();
        struct.setIpAddressIsSet(true);
      }
      if (incoming.get(2)) {
        struct.partnerId = iprot.readString();
        struct.setPartnerIdIsSet(true);
      }
    }
  }

}


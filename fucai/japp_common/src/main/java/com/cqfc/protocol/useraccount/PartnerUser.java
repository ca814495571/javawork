/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.cqfc.protocol.useraccount;

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

public class PartnerUser implements org.apache.thrift.TBase<PartnerUser, PartnerUser._Fields>, java.io.Serializable, Cloneable, Comparable<PartnerUser> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("PartnerUser");

  private static final org.apache.thrift.protocol.TField ID_FIELD_DESC = new org.apache.thrift.protocol.TField("id", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField USER_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("userId", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField PARTNER_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("partnerId", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField PARTNER_SECOND_FIELD_DESC = new org.apache.thrift.protocol.TField("partnerSecond", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField PARTNER_THIRD_FIELD_DESC = new org.apache.thrift.protocol.TField("partnerThird", org.apache.thrift.protocol.TType.STRING, (short)5);
  private static final org.apache.thrift.protocol.TField PARTNER_FOURTH_FIELD_DESC = new org.apache.thrift.protocol.TField("partnerFourth", org.apache.thrift.protocol.TType.STRING, (short)6);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new PartnerUserStandardSchemeFactory());
    schemes.put(TupleScheme.class, new PartnerUserTupleSchemeFactory());
  }

  public int id; // required
  public int userId; // required
  public String partnerId; // required
  public String partnerSecond; // required
  public String partnerThird; // required
  public String partnerFourth; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    ID((short)1, "id"),
    USER_ID((short)2, "userId"),
    PARTNER_ID((short)3, "partnerId"),
    PARTNER_SECOND((short)4, "partnerSecond"),
    PARTNER_THIRD((short)5, "partnerThird"),
    PARTNER_FOURTH((short)6, "partnerFourth");

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
        case 2: // USER_ID
          return USER_ID;
        case 3: // PARTNER_ID
          return PARTNER_ID;
        case 4: // PARTNER_SECOND
          return PARTNER_SECOND;
        case 5: // PARTNER_THIRD
          return PARTNER_THIRD;
        case 6: // PARTNER_FOURTH
          return PARTNER_FOURTH;
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
  private static final int __USERID_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.ID, new org.apache.thrift.meta_data.FieldMetaData("id", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.USER_ID, new org.apache.thrift.meta_data.FieldMetaData("userId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.PARTNER_ID, new org.apache.thrift.meta_data.FieldMetaData("partnerId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PARTNER_SECOND, new org.apache.thrift.meta_data.FieldMetaData("partnerSecond", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PARTNER_THIRD, new org.apache.thrift.meta_data.FieldMetaData("partnerThird", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PARTNER_FOURTH, new org.apache.thrift.meta_data.FieldMetaData("partnerFourth", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(PartnerUser.class, metaDataMap);
  }

  public PartnerUser() {
  }

  public PartnerUser(
    int id,
    int userId,
    String partnerId,
    String partnerSecond,
    String partnerThird,
    String partnerFourth)
  {
    this();
    this.id = id;
    setIdIsSet(true);
    this.userId = userId;
    setUserIdIsSet(true);
    this.partnerId = partnerId;
    this.partnerSecond = partnerSecond;
    this.partnerThird = partnerThird;
    this.partnerFourth = partnerFourth;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public PartnerUser(PartnerUser other) {
    __isset_bitfield = other.__isset_bitfield;
    this.id = other.id;
    this.userId = other.userId;
    if (other.isSetPartnerId()) {
      this.partnerId = other.partnerId;
    }
    if (other.isSetPartnerSecond()) {
      this.partnerSecond = other.partnerSecond;
    }
    if (other.isSetPartnerThird()) {
      this.partnerThird = other.partnerThird;
    }
    if (other.isSetPartnerFourth()) {
      this.partnerFourth = other.partnerFourth;
    }
  }

  public PartnerUser deepCopy() {
    return new PartnerUser(this);
  }

  @Override
  public void clear() {
    setIdIsSet(false);
    this.id = 0;
    setUserIdIsSet(false);
    this.userId = 0;
    this.partnerId = null;
    this.partnerSecond = null;
    this.partnerThird = null;
    this.partnerFourth = null;
  }

  public int getId() {
    return this.id;
  }

  public PartnerUser setId(int id) {
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

  public int getUserId() {
    return this.userId;
  }

  public PartnerUser setUserId(int userId) {
    this.userId = userId;
    setUserIdIsSet(true);
    return this;
  }

  public void unsetUserId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __USERID_ISSET_ID);
  }

  /** Returns true if field userId is set (has been assigned a value) and false otherwise */
  public boolean isSetUserId() {
    return EncodingUtils.testBit(__isset_bitfield, __USERID_ISSET_ID);
  }

  public void setUserIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __USERID_ISSET_ID, value);
  }

  public String getPartnerId() {
    return this.partnerId;
  }

  public PartnerUser setPartnerId(String partnerId) {
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

  public String getPartnerSecond() {
    return this.partnerSecond;
  }

  public PartnerUser setPartnerSecond(String partnerSecond) {
    this.partnerSecond = partnerSecond;
    return this;
  }

  public void unsetPartnerSecond() {
    this.partnerSecond = null;
  }

  /** Returns true if field partnerSecond is set (has been assigned a value) and false otherwise */
  public boolean isSetPartnerSecond() {
    return this.partnerSecond != null;
  }

  public void setPartnerSecondIsSet(boolean value) {
    if (!value) {
      this.partnerSecond = null;
    }
  }

  public String getPartnerThird() {
    return this.partnerThird;
  }

  public PartnerUser setPartnerThird(String partnerThird) {
    this.partnerThird = partnerThird;
    return this;
  }

  public void unsetPartnerThird() {
    this.partnerThird = null;
  }

  /** Returns true if field partnerThird is set (has been assigned a value) and false otherwise */
  public boolean isSetPartnerThird() {
    return this.partnerThird != null;
  }

  public void setPartnerThirdIsSet(boolean value) {
    if (!value) {
      this.partnerThird = null;
    }
  }

  public String getPartnerFourth() {
    return this.partnerFourth;
  }

  public PartnerUser setPartnerFourth(String partnerFourth) {
    this.partnerFourth = partnerFourth;
    return this;
  }

  public void unsetPartnerFourth() {
    this.partnerFourth = null;
  }

  /** Returns true if field partnerFourth is set (has been assigned a value) and false otherwise */
  public boolean isSetPartnerFourth() {
    return this.partnerFourth != null;
  }

  public void setPartnerFourthIsSet(boolean value) {
    if (!value) {
      this.partnerFourth = null;
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

    case USER_ID:
      if (value == null) {
        unsetUserId();
      } else {
        setUserId((Integer)value);
      }
      break;

    case PARTNER_ID:
      if (value == null) {
        unsetPartnerId();
      } else {
        setPartnerId((String)value);
      }
      break;

    case PARTNER_SECOND:
      if (value == null) {
        unsetPartnerSecond();
      } else {
        setPartnerSecond((String)value);
      }
      break;

    case PARTNER_THIRD:
      if (value == null) {
        unsetPartnerThird();
      } else {
        setPartnerThird((String)value);
      }
      break;

    case PARTNER_FOURTH:
      if (value == null) {
        unsetPartnerFourth();
      } else {
        setPartnerFourth((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case ID:
      return Integer.valueOf(getId());

    case USER_ID:
      return Integer.valueOf(getUserId());

    case PARTNER_ID:
      return getPartnerId();

    case PARTNER_SECOND:
      return getPartnerSecond();

    case PARTNER_THIRD:
      return getPartnerThird();

    case PARTNER_FOURTH:
      return getPartnerFourth();

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
    case USER_ID:
      return isSetUserId();
    case PARTNER_ID:
      return isSetPartnerId();
    case PARTNER_SECOND:
      return isSetPartnerSecond();
    case PARTNER_THIRD:
      return isSetPartnerThird();
    case PARTNER_FOURTH:
      return isSetPartnerFourth();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof PartnerUser)
      return this.equals((PartnerUser)that);
    return false;
  }

  public boolean equals(PartnerUser that) {
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

    boolean this_present_userId = true;
    boolean that_present_userId = true;
    if (this_present_userId || that_present_userId) {
      if (!(this_present_userId && that_present_userId))
        return false;
      if (this.userId != that.userId)
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

    boolean this_present_partnerSecond = true && this.isSetPartnerSecond();
    boolean that_present_partnerSecond = true && that.isSetPartnerSecond();
    if (this_present_partnerSecond || that_present_partnerSecond) {
      if (!(this_present_partnerSecond && that_present_partnerSecond))
        return false;
      if (!this.partnerSecond.equals(that.partnerSecond))
        return false;
    }

    boolean this_present_partnerThird = true && this.isSetPartnerThird();
    boolean that_present_partnerThird = true && that.isSetPartnerThird();
    if (this_present_partnerThird || that_present_partnerThird) {
      if (!(this_present_partnerThird && that_present_partnerThird))
        return false;
      if (!this.partnerThird.equals(that.partnerThird))
        return false;
    }

    boolean this_present_partnerFourth = true && this.isSetPartnerFourth();
    boolean that_present_partnerFourth = true && that.isSetPartnerFourth();
    if (this_present_partnerFourth || that_present_partnerFourth) {
      if (!(this_present_partnerFourth && that_present_partnerFourth))
        return false;
      if (!this.partnerFourth.equals(that.partnerFourth))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(PartnerUser other) {
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
    lastComparison = Boolean.valueOf(isSetUserId()).compareTo(other.isSetUserId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUserId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.userId, other.userId);
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
    lastComparison = Boolean.valueOf(isSetPartnerSecond()).compareTo(other.isSetPartnerSecond());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPartnerSecond()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.partnerSecond, other.partnerSecond);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPartnerThird()).compareTo(other.isSetPartnerThird());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPartnerThird()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.partnerThird, other.partnerThird);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPartnerFourth()).compareTo(other.isSetPartnerFourth());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPartnerFourth()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.partnerFourth, other.partnerFourth);
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
    StringBuilder sb = new StringBuilder("PartnerUser(");
    boolean first = true;

    sb.append("id:");
    sb.append(this.id);
    first = false;
    if (!first) sb.append(", ");
    sb.append("userId:");
    sb.append(this.userId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("partnerId:");
    if (this.partnerId == null) {
      sb.append("null");
    } else {
      sb.append(this.partnerId);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("partnerSecond:");
    if (this.partnerSecond == null) {
      sb.append("null");
    } else {
      sb.append(this.partnerSecond);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("partnerThird:");
    if (this.partnerThird == null) {
      sb.append("null");
    } else {
      sb.append(this.partnerThird);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("partnerFourth:");
    if (this.partnerFourth == null) {
      sb.append("null");
    } else {
      sb.append(this.partnerFourth);
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

  private static class PartnerUserStandardSchemeFactory implements SchemeFactory {
    public PartnerUserStandardScheme getScheme() {
      return new PartnerUserStandardScheme();
    }
  }

  private static class PartnerUserStandardScheme extends StandardScheme<PartnerUser> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, PartnerUser struct) throws org.apache.thrift.TException {
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
          case 2: // USER_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.userId = iprot.readI32();
              struct.setUserIdIsSet(true);
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
          case 4: // PARTNER_SECOND
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.partnerSecond = iprot.readString();
              struct.setPartnerSecondIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // PARTNER_THIRD
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.partnerThird = iprot.readString();
              struct.setPartnerThirdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // PARTNER_FOURTH
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.partnerFourth = iprot.readString();
              struct.setPartnerFourthIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, PartnerUser struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(ID_FIELD_DESC);
      oprot.writeI32(struct.id);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(USER_ID_FIELD_DESC);
      oprot.writeI32(struct.userId);
      oprot.writeFieldEnd();
      if (struct.partnerId != null) {
        oprot.writeFieldBegin(PARTNER_ID_FIELD_DESC);
        oprot.writeString(struct.partnerId);
        oprot.writeFieldEnd();
      }
      if (struct.partnerSecond != null) {
        oprot.writeFieldBegin(PARTNER_SECOND_FIELD_DESC);
        oprot.writeString(struct.partnerSecond);
        oprot.writeFieldEnd();
      }
      if (struct.partnerThird != null) {
        oprot.writeFieldBegin(PARTNER_THIRD_FIELD_DESC);
        oprot.writeString(struct.partnerThird);
        oprot.writeFieldEnd();
      }
      if (struct.partnerFourth != null) {
        oprot.writeFieldBegin(PARTNER_FOURTH_FIELD_DESC);
        oprot.writeString(struct.partnerFourth);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class PartnerUserTupleSchemeFactory implements SchemeFactory {
    public PartnerUserTupleScheme getScheme() {
      return new PartnerUserTupleScheme();
    }
  }

  private static class PartnerUserTupleScheme extends TupleScheme<PartnerUser> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, PartnerUser struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetId()) {
        optionals.set(0);
      }
      if (struct.isSetUserId()) {
        optionals.set(1);
      }
      if (struct.isSetPartnerId()) {
        optionals.set(2);
      }
      if (struct.isSetPartnerSecond()) {
        optionals.set(3);
      }
      if (struct.isSetPartnerThird()) {
        optionals.set(4);
      }
      if (struct.isSetPartnerFourth()) {
        optionals.set(5);
      }
      oprot.writeBitSet(optionals, 6);
      if (struct.isSetId()) {
        oprot.writeI32(struct.id);
      }
      if (struct.isSetUserId()) {
        oprot.writeI32(struct.userId);
      }
      if (struct.isSetPartnerId()) {
        oprot.writeString(struct.partnerId);
      }
      if (struct.isSetPartnerSecond()) {
        oprot.writeString(struct.partnerSecond);
      }
      if (struct.isSetPartnerThird()) {
        oprot.writeString(struct.partnerThird);
      }
      if (struct.isSetPartnerFourth()) {
        oprot.writeString(struct.partnerFourth);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, PartnerUser struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(6);
      if (incoming.get(0)) {
        struct.id = iprot.readI32();
        struct.setIdIsSet(true);
      }
      if (incoming.get(1)) {
        struct.userId = iprot.readI32();
        struct.setUserIdIsSet(true);
      }
      if (incoming.get(2)) {
        struct.partnerId = iprot.readString();
        struct.setPartnerIdIsSet(true);
      }
      if (incoming.get(3)) {
        struct.partnerSecond = iprot.readString();
        struct.setPartnerSecondIsSet(true);
      }
      if (incoming.get(4)) {
        struct.partnerThird = iprot.readString();
        struct.setPartnerThirdIsSet(true);
      }
      if (incoming.get(5)) {
        struct.partnerFourth = iprot.readString();
        struct.setPartnerFourthIsSet(true);
      }
    }
  }

}

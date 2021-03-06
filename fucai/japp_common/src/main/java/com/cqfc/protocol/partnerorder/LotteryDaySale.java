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

public class LotteryDaySale implements org.apache.thrift.TBase<LotteryDaySale, LotteryDaySale._Fields>, java.io.Serializable, Cloneable, Comparable<LotteryDaySale> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("LotteryDaySale");

  private static final org.apache.thrift.protocol.TField PARTNER_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("partnerId", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField AWARD_PRIZE_TOTAL_MONEY_FIELD_DESC = new org.apache.thrift.protocol.TField("awardPrizeTotalMoney", org.apache.thrift.protocol.TType.I64, (short)2);
  private static final org.apache.thrift.protocol.TField ENCASH_TOTAL_MONEY_FIELD_DESC = new org.apache.thrift.protocol.TField("encashTotalMoney", org.apache.thrift.protocol.TType.I64, (short)3);
  private static final org.apache.thrift.protocol.TField CHARGE_TOTAL_MONEY_FIELD_DESC = new org.apache.thrift.protocol.TField("chargeTotalMoney", org.apache.thrift.protocol.TType.I64, (short)4);
  private static final org.apache.thrift.protocol.TField SALE_TOTAL_MONEY_FIELD_DESC = new org.apache.thrift.protocol.TField("saleTotalMoney", org.apache.thrift.protocol.TType.I64, (short)5);
  private static final org.apache.thrift.protocol.TField COUNT_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("countTime", org.apache.thrift.protocol.TType.STRING, (short)6);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new LotteryDaySaleStandardSchemeFactory());
    schemes.put(TupleScheme.class, new LotteryDaySaleTupleSchemeFactory());
  }

  public String partnerId; // required
  public long awardPrizeTotalMoney; // required
  public long encashTotalMoney; // required
  public long chargeTotalMoney; // required
  public long saleTotalMoney; // required
  public String countTime; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    PARTNER_ID((short)1, "partnerId"),
    AWARD_PRIZE_TOTAL_MONEY((short)2, "awardPrizeTotalMoney"),
    ENCASH_TOTAL_MONEY((short)3, "encashTotalMoney"),
    CHARGE_TOTAL_MONEY((short)4, "chargeTotalMoney"),
    SALE_TOTAL_MONEY((short)5, "saleTotalMoney"),
    COUNT_TIME((short)6, "countTime");

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
        case 1: // PARTNER_ID
          return PARTNER_ID;
        case 2: // AWARD_PRIZE_TOTAL_MONEY
          return AWARD_PRIZE_TOTAL_MONEY;
        case 3: // ENCASH_TOTAL_MONEY
          return ENCASH_TOTAL_MONEY;
        case 4: // CHARGE_TOTAL_MONEY
          return CHARGE_TOTAL_MONEY;
        case 5: // SALE_TOTAL_MONEY
          return SALE_TOTAL_MONEY;
        case 6: // COUNT_TIME
          return COUNT_TIME;
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
  private static final int __AWARDPRIZETOTALMONEY_ISSET_ID = 0;
  private static final int __ENCASHTOTALMONEY_ISSET_ID = 1;
  private static final int __CHARGETOTALMONEY_ISSET_ID = 2;
  private static final int __SALETOTALMONEY_ISSET_ID = 3;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.PARTNER_ID, new org.apache.thrift.meta_data.FieldMetaData("partnerId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.AWARD_PRIZE_TOTAL_MONEY, new org.apache.thrift.meta_data.FieldMetaData("awardPrizeTotalMoney", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.ENCASH_TOTAL_MONEY, new org.apache.thrift.meta_data.FieldMetaData("encashTotalMoney", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.CHARGE_TOTAL_MONEY, new org.apache.thrift.meta_data.FieldMetaData("chargeTotalMoney", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.SALE_TOTAL_MONEY, new org.apache.thrift.meta_data.FieldMetaData("saleTotalMoney", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.COUNT_TIME, new org.apache.thrift.meta_data.FieldMetaData("countTime", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(LotteryDaySale.class, metaDataMap);
  }

  public LotteryDaySale() {
  }

  public LotteryDaySale(
    String partnerId,
    long awardPrizeTotalMoney,
    long encashTotalMoney,
    long chargeTotalMoney,
    long saleTotalMoney,
    String countTime)
  {
    this();
    this.partnerId = partnerId;
    this.awardPrizeTotalMoney = awardPrizeTotalMoney;
    setAwardPrizeTotalMoneyIsSet(true);
    this.encashTotalMoney = encashTotalMoney;
    setEncashTotalMoneyIsSet(true);
    this.chargeTotalMoney = chargeTotalMoney;
    setChargeTotalMoneyIsSet(true);
    this.saleTotalMoney = saleTotalMoney;
    setSaleTotalMoneyIsSet(true);
    this.countTime = countTime;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public LotteryDaySale(LotteryDaySale other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetPartnerId()) {
      this.partnerId = other.partnerId;
    }
    this.awardPrizeTotalMoney = other.awardPrizeTotalMoney;
    this.encashTotalMoney = other.encashTotalMoney;
    this.chargeTotalMoney = other.chargeTotalMoney;
    this.saleTotalMoney = other.saleTotalMoney;
    if (other.isSetCountTime()) {
      this.countTime = other.countTime;
    }
  }

  public LotteryDaySale deepCopy() {
    return new LotteryDaySale(this);
  }

  @Override
  public void clear() {
    this.partnerId = null;
    setAwardPrizeTotalMoneyIsSet(false);
    this.awardPrizeTotalMoney = 0;
    setEncashTotalMoneyIsSet(false);
    this.encashTotalMoney = 0;
    setChargeTotalMoneyIsSet(false);
    this.chargeTotalMoney = 0;
    setSaleTotalMoneyIsSet(false);
    this.saleTotalMoney = 0;
    this.countTime = null;
  }

  public String getPartnerId() {
    return this.partnerId;
  }

  public LotteryDaySale setPartnerId(String partnerId) {
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

  public long getAwardPrizeTotalMoney() {
    return this.awardPrizeTotalMoney;
  }

  public LotteryDaySale setAwardPrizeTotalMoney(long awardPrizeTotalMoney) {
    this.awardPrizeTotalMoney = awardPrizeTotalMoney;
    setAwardPrizeTotalMoneyIsSet(true);
    return this;
  }

  public void unsetAwardPrizeTotalMoney() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __AWARDPRIZETOTALMONEY_ISSET_ID);
  }

  /** Returns true if field awardPrizeTotalMoney is set (has been assigned a value) and false otherwise */
  public boolean isSetAwardPrizeTotalMoney() {
    return EncodingUtils.testBit(__isset_bitfield, __AWARDPRIZETOTALMONEY_ISSET_ID);
  }

  public void setAwardPrizeTotalMoneyIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __AWARDPRIZETOTALMONEY_ISSET_ID, value);
  }

  public long getEncashTotalMoney() {
    return this.encashTotalMoney;
  }

  public LotteryDaySale setEncashTotalMoney(long encashTotalMoney) {
    this.encashTotalMoney = encashTotalMoney;
    setEncashTotalMoneyIsSet(true);
    return this;
  }

  public void unsetEncashTotalMoney() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __ENCASHTOTALMONEY_ISSET_ID);
  }

  /** Returns true if field encashTotalMoney is set (has been assigned a value) and false otherwise */
  public boolean isSetEncashTotalMoney() {
    return EncodingUtils.testBit(__isset_bitfield, __ENCASHTOTALMONEY_ISSET_ID);
  }

  public void setEncashTotalMoneyIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __ENCASHTOTALMONEY_ISSET_ID, value);
  }

  public long getChargeTotalMoney() {
    return this.chargeTotalMoney;
  }

  public LotteryDaySale setChargeTotalMoney(long chargeTotalMoney) {
    this.chargeTotalMoney = chargeTotalMoney;
    setChargeTotalMoneyIsSet(true);
    return this;
  }

  public void unsetChargeTotalMoney() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __CHARGETOTALMONEY_ISSET_ID);
  }

  /** Returns true if field chargeTotalMoney is set (has been assigned a value) and false otherwise */
  public boolean isSetChargeTotalMoney() {
    return EncodingUtils.testBit(__isset_bitfield, __CHARGETOTALMONEY_ISSET_ID);
  }

  public void setChargeTotalMoneyIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __CHARGETOTALMONEY_ISSET_ID, value);
  }

  public long getSaleTotalMoney() {
    return this.saleTotalMoney;
  }

  public LotteryDaySale setSaleTotalMoney(long saleTotalMoney) {
    this.saleTotalMoney = saleTotalMoney;
    setSaleTotalMoneyIsSet(true);
    return this;
  }

  public void unsetSaleTotalMoney() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __SALETOTALMONEY_ISSET_ID);
  }

  /** Returns true if field saleTotalMoney is set (has been assigned a value) and false otherwise */
  public boolean isSetSaleTotalMoney() {
    return EncodingUtils.testBit(__isset_bitfield, __SALETOTALMONEY_ISSET_ID);
  }

  public void setSaleTotalMoneyIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __SALETOTALMONEY_ISSET_ID, value);
  }

  public String getCountTime() {
    return this.countTime;
  }

  public LotteryDaySale setCountTime(String countTime) {
    this.countTime = countTime;
    return this;
  }

  public void unsetCountTime() {
    this.countTime = null;
  }

  /** Returns true if field countTime is set (has been assigned a value) and false otherwise */
  public boolean isSetCountTime() {
    return this.countTime != null;
  }

  public void setCountTimeIsSet(boolean value) {
    if (!value) {
      this.countTime = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case PARTNER_ID:
      if (value == null) {
        unsetPartnerId();
      } else {
        setPartnerId((String)value);
      }
      break;

    case AWARD_PRIZE_TOTAL_MONEY:
      if (value == null) {
        unsetAwardPrizeTotalMoney();
      } else {
        setAwardPrizeTotalMoney((Long)value);
      }
      break;

    case ENCASH_TOTAL_MONEY:
      if (value == null) {
        unsetEncashTotalMoney();
      } else {
        setEncashTotalMoney((Long)value);
      }
      break;

    case CHARGE_TOTAL_MONEY:
      if (value == null) {
        unsetChargeTotalMoney();
      } else {
        setChargeTotalMoney((Long)value);
      }
      break;

    case SALE_TOTAL_MONEY:
      if (value == null) {
        unsetSaleTotalMoney();
      } else {
        setSaleTotalMoney((Long)value);
      }
      break;

    case COUNT_TIME:
      if (value == null) {
        unsetCountTime();
      } else {
        setCountTime((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case PARTNER_ID:
      return getPartnerId();

    case AWARD_PRIZE_TOTAL_MONEY:
      return Long.valueOf(getAwardPrizeTotalMoney());

    case ENCASH_TOTAL_MONEY:
      return Long.valueOf(getEncashTotalMoney());

    case CHARGE_TOTAL_MONEY:
      return Long.valueOf(getChargeTotalMoney());

    case SALE_TOTAL_MONEY:
      return Long.valueOf(getSaleTotalMoney());

    case COUNT_TIME:
      return getCountTime();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case PARTNER_ID:
      return isSetPartnerId();
    case AWARD_PRIZE_TOTAL_MONEY:
      return isSetAwardPrizeTotalMoney();
    case ENCASH_TOTAL_MONEY:
      return isSetEncashTotalMoney();
    case CHARGE_TOTAL_MONEY:
      return isSetChargeTotalMoney();
    case SALE_TOTAL_MONEY:
      return isSetSaleTotalMoney();
    case COUNT_TIME:
      return isSetCountTime();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof LotteryDaySale)
      return this.equals((LotteryDaySale)that);
    return false;
  }

  public boolean equals(LotteryDaySale that) {
    if (that == null)
      return false;

    boolean this_present_partnerId = true && this.isSetPartnerId();
    boolean that_present_partnerId = true && that.isSetPartnerId();
    if (this_present_partnerId || that_present_partnerId) {
      if (!(this_present_partnerId && that_present_partnerId))
        return false;
      if (!this.partnerId.equals(that.partnerId))
        return false;
    }

    boolean this_present_awardPrizeTotalMoney = true;
    boolean that_present_awardPrizeTotalMoney = true;
    if (this_present_awardPrizeTotalMoney || that_present_awardPrizeTotalMoney) {
      if (!(this_present_awardPrizeTotalMoney && that_present_awardPrizeTotalMoney))
        return false;
      if (this.awardPrizeTotalMoney != that.awardPrizeTotalMoney)
        return false;
    }

    boolean this_present_encashTotalMoney = true;
    boolean that_present_encashTotalMoney = true;
    if (this_present_encashTotalMoney || that_present_encashTotalMoney) {
      if (!(this_present_encashTotalMoney && that_present_encashTotalMoney))
        return false;
      if (this.encashTotalMoney != that.encashTotalMoney)
        return false;
    }

    boolean this_present_chargeTotalMoney = true;
    boolean that_present_chargeTotalMoney = true;
    if (this_present_chargeTotalMoney || that_present_chargeTotalMoney) {
      if (!(this_present_chargeTotalMoney && that_present_chargeTotalMoney))
        return false;
      if (this.chargeTotalMoney != that.chargeTotalMoney)
        return false;
    }

    boolean this_present_saleTotalMoney = true;
    boolean that_present_saleTotalMoney = true;
    if (this_present_saleTotalMoney || that_present_saleTotalMoney) {
      if (!(this_present_saleTotalMoney && that_present_saleTotalMoney))
        return false;
      if (this.saleTotalMoney != that.saleTotalMoney)
        return false;
    }

    boolean this_present_countTime = true && this.isSetCountTime();
    boolean that_present_countTime = true && that.isSetCountTime();
    if (this_present_countTime || that_present_countTime) {
      if (!(this_present_countTime && that_present_countTime))
        return false;
      if (!this.countTime.equals(that.countTime))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(LotteryDaySale other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

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
    lastComparison = Boolean.valueOf(isSetAwardPrizeTotalMoney()).compareTo(other.isSetAwardPrizeTotalMoney());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAwardPrizeTotalMoney()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.awardPrizeTotalMoney, other.awardPrizeTotalMoney);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetEncashTotalMoney()).compareTo(other.isSetEncashTotalMoney());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEncashTotalMoney()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.encashTotalMoney, other.encashTotalMoney);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetChargeTotalMoney()).compareTo(other.isSetChargeTotalMoney());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetChargeTotalMoney()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.chargeTotalMoney, other.chargeTotalMoney);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetSaleTotalMoney()).compareTo(other.isSetSaleTotalMoney());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSaleTotalMoney()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.saleTotalMoney, other.saleTotalMoney);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetCountTime()).compareTo(other.isSetCountTime());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCountTime()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.countTime, other.countTime);
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
    StringBuilder sb = new StringBuilder("LotteryDaySale(");
    boolean first = true;

    sb.append("partnerId:");
    if (this.partnerId == null) {
      sb.append("null");
    } else {
      sb.append(this.partnerId);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("awardPrizeTotalMoney:");
    sb.append(this.awardPrizeTotalMoney);
    first = false;
    if (!first) sb.append(", ");
    sb.append("encashTotalMoney:");
    sb.append(this.encashTotalMoney);
    first = false;
    if (!first) sb.append(", ");
    sb.append("chargeTotalMoney:");
    sb.append(this.chargeTotalMoney);
    first = false;
    if (!first) sb.append(", ");
    sb.append("saleTotalMoney:");
    sb.append(this.saleTotalMoney);
    first = false;
    if (!first) sb.append(", ");
    sb.append("countTime:");
    if (this.countTime == null) {
      sb.append("null");
    } else {
      sb.append(this.countTime);
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

  private static class LotteryDaySaleStandardSchemeFactory implements SchemeFactory {
    public LotteryDaySaleStandardScheme getScheme() {
      return new LotteryDaySaleStandardScheme();
    }
  }

  private static class LotteryDaySaleStandardScheme extends StandardScheme<LotteryDaySale> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, LotteryDaySale struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // PARTNER_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.partnerId = iprot.readString();
              struct.setPartnerIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // AWARD_PRIZE_TOTAL_MONEY
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.awardPrizeTotalMoney = iprot.readI64();
              struct.setAwardPrizeTotalMoneyIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // ENCASH_TOTAL_MONEY
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.encashTotalMoney = iprot.readI64();
              struct.setEncashTotalMoneyIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // CHARGE_TOTAL_MONEY
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.chargeTotalMoney = iprot.readI64();
              struct.setChargeTotalMoneyIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // SALE_TOTAL_MONEY
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.saleTotalMoney = iprot.readI64();
              struct.setSaleTotalMoneyIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // COUNT_TIME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.countTime = iprot.readString();
              struct.setCountTimeIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, LotteryDaySale struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.partnerId != null) {
        oprot.writeFieldBegin(PARTNER_ID_FIELD_DESC);
        oprot.writeString(struct.partnerId);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(AWARD_PRIZE_TOTAL_MONEY_FIELD_DESC);
      oprot.writeI64(struct.awardPrizeTotalMoney);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(ENCASH_TOTAL_MONEY_FIELD_DESC);
      oprot.writeI64(struct.encashTotalMoney);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(CHARGE_TOTAL_MONEY_FIELD_DESC);
      oprot.writeI64(struct.chargeTotalMoney);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(SALE_TOTAL_MONEY_FIELD_DESC);
      oprot.writeI64(struct.saleTotalMoney);
      oprot.writeFieldEnd();
      if (struct.countTime != null) {
        oprot.writeFieldBegin(COUNT_TIME_FIELD_DESC);
        oprot.writeString(struct.countTime);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class LotteryDaySaleTupleSchemeFactory implements SchemeFactory {
    public LotteryDaySaleTupleScheme getScheme() {
      return new LotteryDaySaleTupleScheme();
    }
  }

  private static class LotteryDaySaleTupleScheme extends TupleScheme<LotteryDaySale> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, LotteryDaySale struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetPartnerId()) {
        optionals.set(0);
      }
      if (struct.isSetAwardPrizeTotalMoney()) {
        optionals.set(1);
      }
      if (struct.isSetEncashTotalMoney()) {
        optionals.set(2);
      }
      if (struct.isSetChargeTotalMoney()) {
        optionals.set(3);
      }
      if (struct.isSetSaleTotalMoney()) {
        optionals.set(4);
      }
      if (struct.isSetCountTime()) {
        optionals.set(5);
      }
      oprot.writeBitSet(optionals, 6);
      if (struct.isSetPartnerId()) {
        oprot.writeString(struct.partnerId);
      }
      if (struct.isSetAwardPrizeTotalMoney()) {
        oprot.writeI64(struct.awardPrizeTotalMoney);
      }
      if (struct.isSetEncashTotalMoney()) {
        oprot.writeI64(struct.encashTotalMoney);
      }
      if (struct.isSetChargeTotalMoney()) {
        oprot.writeI64(struct.chargeTotalMoney);
      }
      if (struct.isSetSaleTotalMoney()) {
        oprot.writeI64(struct.saleTotalMoney);
      }
      if (struct.isSetCountTime()) {
        oprot.writeString(struct.countTime);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, LotteryDaySale struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(6);
      if (incoming.get(0)) {
        struct.partnerId = iprot.readString();
        struct.setPartnerIdIsSet(true);
      }
      if (incoming.get(1)) {
        struct.awardPrizeTotalMoney = iprot.readI64();
        struct.setAwardPrizeTotalMoneyIsSet(true);
      }
      if (incoming.get(2)) {
        struct.encashTotalMoney = iprot.readI64();
        struct.setEncashTotalMoneyIsSet(true);
      }
      if (incoming.get(3)) {
        struct.chargeTotalMoney = iprot.readI64();
        struct.setChargeTotalMoneyIsSet(true);
      }
      if (incoming.get(4)) {
        struct.saleTotalMoney = iprot.readI64();
        struct.setSaleTotalMoneyIsSet(true);
      }
      if (incoming.get(5)) {
        struct.countTime = iprot.readString();
        struct.setCountTimeIsSet(true);
      }
    }
  }

}


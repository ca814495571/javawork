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

public class UserAccount implements org.apache.thrift.TBase<UserAccount, UserAccount._Fields>, java.io.Serializable, Cloneable, Comparable<UserAccount> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("UserAccount");

  private static final org.apache.thrift.protocol.TField USER_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("userId", org.apache.thrift.protocol.TType.I64, (short)1);
  private static final org.apache.thrift.protocol.TField TOTAL_AMOUNT_FIELD_DESC = new org.apache.thrift.protocol.TField("totalAmount", org.apache.thrift.protocol.TType.I64, (short)2);
  private static final org.apache.thrift.protocol.TField FREEZE_AMOUNT_FIELD_DESC = new org.apache.thrift.protocol.TField("freezeAmount", org.apache.thrift.protocol.TType.I64, (short)3);
  private static final org.apache.thrift.protocol.TField STATE_FIELD_DESC = new org.apache.thrift.protocol.TField("state", org.apache.thrift.protocol.TType.I32, (short)4);
  private static final org.apache.thrift.protocol.TField USABLE_AMOUNT_FIELD_DESC = new org.apache.thrift.protocol.TField("usableAmount", org.apache.thrift.protocol.TType.I64, (short)5);
  private static final org.apache.thrift.protocol.TField CREATE_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("createTime", org.apache.thrift.protocol.TType.STRING, (short)6);
  private static final org.apache.thrift.protocol.TField LAST_UPDATE_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("lastUpdateTime", org.apache.thrift.protocol.TType.STRING, (short)7);
  private static final org.apache.thrift.protocol.TField USER_HANDSEL_LIST_FIELD_DESC = new org.apache.thrift.protocol.TField("userHandselList", org.apache.thrift.protocol.TType.LIST, (short)8);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new UserAccountStandardSchemeFactory());
    schemes.put(TupleScheme.class, new UserAccountTupleSchemeFactory());
  }

  public long userId; // required
  public long totalAmount; // required
  public long freezeAmount; // required
  public int state; // required
  public long usableAmount; // required
  public String createTime; // required
  public String lastUpdateTime; // required
  public List<UserHandsel> userHandselList; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    USER_ID((short)1, "userId"),
    TOTAL_AMOUNT((short)2, "totalAmount"),
    FREEZE_AMOUNT((short)3, "freezeAmount"),
    STATE((short)4, "state"),
    USABLE_AMOUNT((short)5, "usableAmount"),
    CREATE_TIME((short)6, "createTime"),
    LAST_UPDATE_TIME((short)7, "lastUpdateTime"),
    USER_HANDSEL_LIST((short)8, "userHandselList");

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
        case 1: // USER_ID
          return USER_ID;
        case 2: // TOTAL_AMOUNT
          return TOTAL_AMOUNT;
        case 3: // FREEZE_AMOUNT
          return FREEZE_AMOUNT;
        case 4: // STATE
          return STATE;
        case 5: // USABLE_AMOUNT
          return USABLE_AMOUNT;
        case 6: // CREATE_TIME
          return CREATE_TIME;
        case 7: // LAST_UPDATE_TIME
          return LAST_UPDATE_TIME;
        case 8: // USER_HANDSEL_LIST
          return USER_HANDSEL_LIST;
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
  private static final int __USERID_ISSET_ID = 0;
  private static final int __TOTALAMOUNT_ISSET_ID = 1;
  private static final int __FREEZEAMOUNT_ISSET_ID = 2;
  private static final int __STATE_ISSET_ID = 3;
  private static final int __USABLEAMOUNT_ISSET_ID = 4;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.USER_ID, new org.apache.thrift.meta_data.FieldMetaData("userId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.TOTAL_AMOUNT, new org.apache.thrift.meta_data.FieldMetaData("totalAmount", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.FREEZE_AMOUNT, new org.apache.thrift.meta_data.FieldMetaData("freezeAmount", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.STATE, new org.apache.thrift.meta_data.FieldMetaData("state", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.USABLE_AMOUNT, new org.apache.thrift.meta_data.FieldMetaData("usableAmount", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.CREATE_TIME, new org.apache.thrift.meta_data.FieldMetaData("createTime", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.LAST_UPDATE_TIME, new org.apache.thrift.meta_data.FieldMetaData("lastUpdateTime", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.USER_HANDSEL_LIST, new org.apache.thrift.meta_data.FieldMetaData("userHandselList", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, UserHandsel.class))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(UserAccount.class, metaDataMap);
  }

  public UserAccount() {
  }

  public UserAccount(
    long userId,
    long totalAmount,
    long freezeAmount,
    int state,
    long usableAmount,
    String createTime,
    String lastUpdateTime,
    List<UserHandsel> userHandselList)
  {
    this();
    this.userId = userId;
    setUserIdIsSet(true);
    this.totalAmount = totalAmount;
    setTotalAmountIsSet(true);
    this.freezeAmount = freezeAmount;
    setFreezeAmountIsSet(true);
    this.state = state;
    setStateIsSet(true);
    this.usableAmount = usableAmount;
    setUsableAmountIsSet(true);
    this.createTime = createTime;
    this.lastUpdateTime = lastUpdateTime;
    this.userHandselList = userHandselList;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public UserAccount(UserAccount other) {
    __isset_bitfield = other.__isset_bitfield;
    this.userId = other.userId;
    this.totalAmount = other.totalAmount;
    this.freezeAmount = other.freezeAmount;
    this.state = other.state;
    this.usableAmount = other.usableAmount;
    if (other.isSetCreateTime()) {
      this.createTime = other.createTime;
    }
    if (other.isSetLastUpdateTime()) {
      this.lastUpdateTime = other.lastUpdateTime;
    }
    if (other.isSetUserHandselList()) {
      List<UserHandsel> __this__userHandselList = new ArrayList<UserHandsel>(other.userHandselList.size());
      for (UserHandsel other_element : other.userHandselList) {
        __this__userHandselList.add(new UserHandsel(other_element));
      }
      this.userHandselList = __this__userHandselList;
    }
  }

  public UserAccount deepCopy() {
    return new UserAccount(this);
  }

  @Override
  public void clear() {
    setUserIdIsSet(false);
    this.userId = 0;
    setTotalAmountIsSet(false);
    this.totalAmount = 0;
    setFreezeAmountIsSet(false);
    this.freezeAmount = 0;
    setStateIsSet(false);
    this.state = 0;
    setUsableAmountIsSet(false);
    this.usableAmount = 0;
    this.createTime = null;
    this.lastUpdateTime = null;
    this.userHandselList = null;
  }

  public long getUserId() {
    return this.userId;
  }

  public UserAccount setUserId(long userId) {
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

  public long getTotalAmount() {
    return this.totalAmount;
  }

  public UserAccount setTotalAmount(long totalAmount) {
    this.totalAmount = totalAmount;
    setTotalAmountIsSet(true);
    return this;
  }

  public void unsetTotalAmount() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __TOTALAMOUNT_ISSET_ID);
  }

  /** Returns true if field totalAmount is set (has been assigned a value) and false otherwise */
  public boolean isSetTotalAmount() {
    return EncodingUtils.testBit(__isset_bitfield, __TOTALAMOUNT_ISSET_ID);
  }

  public void setTotalAmountIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __TOTALAMOUNT_ISSET_ID, value);
  }

  public long getFreezeAmount() {
    return this.freezeAmount;
  }

  public UserAccount setFreezeAmount(long freezeAmount) {
    this.freezeAmount = freezeAmount;
    setFreezeAmountIsSet(true);
    return this;
  }

  public void unsetFreezeAmount() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __FREEZEAMOUNT_ISSET_ID);
  }

  /** Returns true if field freezeAmount is set (has been assigned a value) and false otherwise */
  public boolean isSetFreezeAmount() {
    return EncodingUtils.testBit(__isset_bitfield, __FREEZEAMOUNT_ISSET_ID);
  }

  public void setFreezeAmountIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __FREEZEAMOUNT_ISSET_ID, value);
  }

  public int getState() {
    return this.state;
  }

  public UserAccount setState(int state) {
    this.state = state;
    setStateIsSet(true);
    return this;
  }

  public void unsetState() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __STATE_ISSET_ID);
  }

  /** Returns true if field state is set (has been assigned a value) and false otherwise */
  public boolean isSetState() {
    return EncodingUtils.testBit(__isset_bitfield, __STATE_ISSET_ID);
  }

  public void setStateIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __STATE_ISSET_ID, value);
  }

  public long getUsableAmount() {
    return this.usableAmount;
  }

  public UserAccount setUsableAmount(long usableAmount) {
    this.usableAmount = usableAmount;
    setUsableAmountIsSet(true);
    return this;
  }

  public void unsetUsableAmount() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __USABLEAMOUNT_ISSET_ID);
  }

  /** Returns true if field usableAmount is set (has been assigned a value) and false otherwise */
  public boolean isSetUsableAmount() {
    return EncodingUtils.testBit(__isset_bitfield, __USABLEAMOUNT_ISSET_ID);
  }

  public void setUsableAmountIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __USABLEAMOUNT_ISSET_ID, value);
  }

  public String getCreateTime() {
    return this.createTime;
  }

  public UserAccount setCreateTime(String createTime) {
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

  public UserAccount setLastUpdateTime(String lastUpdateTime) {
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

  public int getUserHandselListSize() {
    return (this.userHandselList == null) ? 0 : this.userHandselList.size();
  }

  public java.util.Iterator<UserHandsel> getUserHandselListIterator() {
    return (this.userHandselList == null) ? null : this.userHandselList.iterator();
  }

  public void addToUserHandselList(UserHandsel elem) {
    if (this.userHandselList == null) {
      this.userHandselList = new ArrayList<UserHandsel>();
    }
    this.userHandselList.add(elem);
  }

  public List<UserHandsel> getUserHandselList() {
    return this.userHandselList;
  }

  public UserAccount setUserHandselList(List<UserHandsel> userHandselList) {
    this.userHandselList = userHandselList;
    return this;
  }

  public void unsetUserHandselList() {
    this.userHandselList = null;
  }

  /** Returns true if field userHandselList is set (has been assigned a value) and false otherwise */
  public boolean isSetUserHandselList() {
    return this.userHandselList != null;
  }

  public void setUserHandselListIsSet(boolean value) {
    if (!value) {
      this.userHandselList = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case USER_ID:
      if (value == null) {
        unsetUserId();
      } else {
        setUserId((Long)value);
      }
      break;

    case TOTAL_AMOUNT:
      if (value == null) {
        unsetTotalAmount();
      } else {
        setTotalAmount((Long)value);
      }
      break;

    case FREEZE_AMOUNT:
      if (value == null) {
        unsetFreezeAmount();
      } else {
        setFreezeAmount((Long)value);
      }
      break;

    case STATE:
      if (value == null) {
        unsetState();
      } else {
        setState((Integer)value);
      }
      break;

    case USABLE_AMOUNT:
      if (value == null) {
        unsetUsableAmount();
      } else {
        setUsableAmount((Long)value);
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

    case USER_HANDSEL_LIST:
      if (value == null) {
        unsetUserHandselList();
      } else {
        setUserHandselList((List<UserHandsel>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case USER_ID:
      return Long.valueOf(getUserId());

    case TOTAL_AMOUNT:
      return Long.valueOf(getTotalAmount());

    case FREEZE_AMOUNT:
      return Long.valueOf(getFreezeAmount());

    case STATE:
      return Integer.valueOf(getState());

    case USABLE_AMOUNT:
      return Long.valueOf(getUsableAmount());

    case CREATE_TIME:
      return getCreateTime();

    case LAST_UPDATE_TIME:
      return getLastUpdateTime();

    case USER_HANDSEL_LIST:
      return getUserHandselList();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case USER_ID:
      return isSetUserId();
    case TOTAL_AMOUNT:
      return isSetTotalAmount();
    case FREEZE_AMOUNT:
      return isSetFreezeAmount();
    case STATE:
      return isSetState();
    case USABLE_AMOUNT:
      return isSetUsableAmount();
    case CREATE_TIME:
      return isSetCreateTime();
    case LAST_UPDATE_TIME:
      return isSetLastUpdateTime();
    case USER_HANDSEL_LIST:
      return isSetUserHandselList();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof UserAccount)
      return this.equals((UserAccount)that);
    return false;
  }

  public boolean equals(UserAccount that) {
    if (that == null)
      return false;

    boolean this_present_userId = true;
    boolean that_present_userId = true;
    if (this_present_userId || that_present_userId) {
      if (!(this_present_userId && that_present_userId))
        return false;
      if (this.userId != that.userId)
        return false;
    }

    boolean this_present_totalAmount = true;
    boolean that_present_totalAmount = true;
    if (this_present_totalAmount || that_present_totalAmount) {
      if (!(this_present_totalAmount && that_present_totalAmount))
        return false;
      if (this.totalAmount != that.totalAmount)
        return false;
    }

    boolean this_present_freezeAmount = true;
    boolean that_present_freezeAmount = true;
    if (this_present_freezeAmount || that_present_freezeAmount) {
      if (!(this_present_freezeAmount && that_present_freezeAmount))
        return false;
      if (this.freezeAmount != that.freezeAmount)
        return false;
    }

    boolean this_present_state = true;
    boolean that_present_state = true;
    if (this_present_state || that_present_state) {
      if (!(this_present_state && that_present_state))
        return false;
      if (this.state != that.state)
        return false;
    }

    boolean this_present_usableAmount = true;
    boolean that_present_usableAmount = true;
    if (this_present_usableAmount || that_present_usableAmount) {
      if (!(this_present_usableAmount && that_present_usableAmount))
        return false;
      if (this.usableAmount != that.usableAmount)
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

    boolean this_present_userHandselList = true && this.isSetUserHandselList();
    boolean that_present_userHandselList = true && that.isSetUserHandselList();
    if (this_present_userHandselList || that_present_userHandselList) {
      if (!(this_present_userHandselList && that_present_userHandselList))
        return false;
      if (!this.userHandselList.equals(that.userHandselList))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(UserAccount other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

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
    lastComparison = Boolean.valueOf(isSetTotalAmount()).compareTo(other.isSetTotalAmount());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTotalAmount()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.totalAmount, other.totalAmount);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetFreezeAmount()).compareTo(other.isSetFreezeAmount());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetFreezeAmount()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.freezeAmount, other.freezeAmount);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetState()).compareTo(other.isSetState());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetState()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.state, other.state);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetUsableAmount()).compareTo(other.isSetUsableAmount());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUsableAmount()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.usableAmount, other.usableAmount);
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
    lastComparison = Boolean.valueOf(isSetUserHandselList()).compareTo(other.isSetUserHandselList());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUserHandselList()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.userHandselList, other.userHandselList);
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
    StringBuilder sb = new StringBuilder("UserAccount(");
    boolean first = true;

    sb.append("userId:");
    sb.append(this.userId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("totalAmount:");
    sb.append(this.totalAmount);
    first = false;
    if (!first) sb.append(", ");
    sb.append("freezeAmount:");
    sb.append(this.freezeAmount);
    first = false;
    if (!first) sb.append(", ");
    sb.append("state:");
    sb.append(this.state);
    first = false;
    if (!first) sb.append(", ");
    sb.append("usableAmount:");
    sb.append(this.usableAmount);
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
    if (!first) sb.append(", ");
    sb.append("userHandselList:");
    if (this.userHandselList == null) {
      sb.append("null");
    } else {
      sb.append(this.userHandselList);
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

  private static class UserAccountStandardSchemeFactory implements SchemeFactory {
    public UserAccountStandardScheme getScheme() {
      return new UserAccountStandardScheme();
    }
  }

  private static class UserAccountStandardScheme extends StandardScheme<UserAccount> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, UserAccount struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // USER_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.userId = iprot.readI64();
              struct.setUserIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // TOTAL_AMOUNT
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.totalAmount = iprot.readI64();
              struct.setTotalAmountIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // FREEZE_AMOUNT
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.freezeAmount = iprot.readI64();
              struct.setFreezeAmountIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // STATE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.state = iprot.readI32();
              struct.setStateIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // USABLE_AMOUNT
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.usableAmount = iprot.readI64();
              struct.setUsableAmountIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // CREATE_TIME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.createTime = iprot.readString();
              struct.setCreateTimeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 7: // LAST_UPDATE_TIME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.lastUpdateTime = iprot.readString();
              struct.setLastUpdateTimeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 8: // USER_HANDSEL_LIST
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list0 = iprot.readListBegin();
                struct.userHandselList = new ArrayList<UserHandsel>(_list0.size);
                for (int _i1 = 0; _i1 < _list0.size; ++_i1)
                {
                  UserHandsel _elem2;
                  _elem2 = new UserHandsel();
                  _elem2.read(iprot);
                  struct.userHandselList.add(_elem2);
                }
                iprot.readListEnd();
              }
              struct.setUserHandselListIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, UserAccount struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(USER_ID_FIELD_DESC);
      oprot.writeI64(struct.userId);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(TOTAL_AMOUNT_FIELD_DESC);
      oprot.writeI64(struct.totalAmount);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(FREEZE_AMOUNT_FIELD_DESC);
      oprot.writeI64(struct.freezeAmount);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(STATE_FIELD_DESC);
      oprot.writeI32(struct.state);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(USABLE_AMOUNT_FIELD_DESC);
      oprot.writeI64(struct.usableAmount);
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
      if (struct.userHandselList != null) {
        oprot.writeFieldBegin(USER_HANDSEL_LIST_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.userHandselList.size()));
          for (UserHandsel _iter3 : struct.userHandselList)
          {
            _iter3.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class UserAccountTupleSchemeFactory implements SchemeFactory {
    public UserAccountTupleScheme getScheme() {
      return new UserAccountTupleScheme();
    }
  }

  private static class UserAccountTupleScheme extends TupleScheme<UserAccount> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, UserAccount struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetUserId()) {
        optionals.set(0);
      }
      if (struct.isSetTotalAmount()) {
        optionals.set(1);
      }
      if (struct.isSetFreezeAmount()) {
        optionals.set(2);
      }
      if (struct.isSetState()) {
        optionals.set(3);
      }
      if (struct.isSetUsableAmount()) {
        optionals.set(4);
      }
      if (struct.isSetCreateTime()) {
        optionals.set(5);
      }
      if (struct.isSetLastUpdateTime()) {
        optionals.set(6);
      }
      if (struct.isSetUserHandselList()) {
        optionals.set(7);
      }
      oprot.writeBitSet(optionals, 8);
      if (struct.isSetUserId()) {
        oprot.writeI64(struct.userId);
      }
      if (struct.isSetTotalAmount()) {
        oprot.writeI64(struct.totalAmount);
      }
      if (struct.isSetFreezeAmount()) {
        oprot.writeI64(struct.freezeAmount);
      }
      if (struct.isSetState()) {
        oprot.writeI32(struct.state);
      }
      if (struct.isSetUsableAmount()) {
        oprot.writeI64(struct.usableAmount);
      }
      if (struct.isSetCreateTime()) {
        oprot.writeString(struct.createTime);
      }
      if (struct.isSetLastUpdateTime()) {
        oprot.writeString(struct.lastUpdateTime);
      }
      if (struct.isSetUserHandselList()) {
        {
          oprot.writeI32(struct.userHandselList.size());
          for (UserHandsel _iter4 : struct.userHandselList)
          {
            _iter4.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, UserAccount struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(8);
      if (incoming.get(0)) {
        struct.userId = iprot.readI64();
        struct.setUserIdIsSet(true);
      }
      if (incoming.get(1)) {
        struct.totalAmount = iprot.readI64();
        struct.setTotalAmountIsSet(true);
      }
      if (incoming.get(2)) {
        struct.freezeAmount = iprot.readI64();
        struct.setFreezeAmountIsSet(true);
      }
      if (incoming.get(3)) {
        struct.state = iprot.readI32();
        struct.setStateIsSet(true);
      }
      if (incoming.get(4)) {
        struct.usableAmount = iprot.readI64();
        struct.setUsableAmountIsSet(true);
      }
      if (incoming.get(5)) {
        struct.createTime = iprot.readString();
        struct.setCreateTimeIsSet(true);
      }
      if (incoming.get(6)) {
        struct.lastUpdateTime = iprot.readString();
        struct.setLastUpdateTimeIsSet(true);
      }
      if (incoming.get(7)) {
        {
          org.apache.thrift.protocol.TList _list5 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.userHandselList = new ArrayList<UserHandsel>(_list5.size);
          for (int _i6 = 0; _i6 < _list5.size; ++_i6)
          {
            UserHandsel _elem7;
            _elem7 = new UserHandsel();
            _elem7.read(iprot);
            struct.userHandselList.add(_elem7);
          }
        }
        struct.setUserHandselListIsSet(true);
      }
    }
  }

}


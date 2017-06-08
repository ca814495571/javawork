/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.cqfc.protocol.lotteryissue;

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

public class DrawResultData implements org.apache.thrift.TBase<DrawResultData, DrawResultData._Fields>, java.io.Serializable, Cloneable, Comparable<DrawResultData> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("DrawResultData");

  private static final org.apache.thrift.protocol.TField CURRENT_PAGE_FIELD_DESC = new org.apache.thrift.protocol.TField("currentPage", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField PAGE_SIZE_FIELD_DESC = new org.apache.thrift.protocol.TField("pageSize", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField TOTAL_SIZE_FIELD_DESC = new org.apache.thrift.protocol.TField("totalSize", org.apache.thrift.protocol.TType.I32, (short)3);
  private static final org.apache.thrift.protocol.TField RESULT_LIST_FIELD_DESC = new org.apache.thrift.protocol.TField("resultList", org.apache.thrift.protocol.TType.LIST, (short)4);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new DrawResultDataStandardSchemeFactory());
    schemes.put(TupleScheme.class, new DrawResultDataTupleSchemeFactory());
  }

  public int currentPage; // required
  public int pageSize; // required
  public int totalSize; // required
  public List<LotteryDrawResult> resultList; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    CURRENT_PAGE((short)1, "currentPage"),
    PAGE_SIZE((short)2, "pageSize"),
    TOTAL_SIZE((short)3, "totalSize"),
    RESULT_LIST((short)4, "resultList");

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
        case 1: // CURRENT_PAGE
          return CURRENT_PAGE;
        case 2: // PAGE_SIZE
          return PAGE_SIZE;
        case 3: // TOTAL_SIZE
          return TOTAL_SIZE;
        case 4: // RESULT_LIST
          return RESULT_LIST;
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
  private static final int __CURRENTPAGE_ISSET_ID = 0;
  private static final int __PAGESIZE_ISSET_ID = 1;
  private static final int __TOTALSIZE_ISSET_ID = 2;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.CURRENT_PAGE, new org.apache.thrift.meta_data.FieldMetaData("currentPage", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.PAGE_SIZE, new org.apache.thrift.meta_data.FieldMetaData("pageSize", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.TOTAL_SIZE, new org.apache.thrift.meta_data.FieldMetaData("totalSize", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.RESULT_LIST, new org.apache.thrift.meta_data.FieldMetaData("resultList", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, LotteryDrawResult.class))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(DrawResultData.class, metaDataMap);
  }

  public DrawResultData() {
  }

  public DrawResultData(
    int currentPage,
    int pageSize,
    int totalSize,
    List<LotteryDrawResult> resultList)
  {
    this();
    this.currentPage = currentPage;
    setCurrentPageIsSet(true);
    this.pageSize = pageSize;
    setPageSizeIsSet(true);
    this.totalSize = totalSize;
    setTotalSizeIsSet(true);
    this.resultList = resultList;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public DrawResultData(DrawResultData other) {
    __isset_bitfield = other.__isset_bitfield;
    this.currentPage = other.currentPage;
    this.pageSize = other.pageSize;
    this.totalSize = other.totalSize;
    if (other.isSetResultList()) {
      List<LotteryDrawResult> __this__resultList = new ArrayList<LotteryDrawResult>(other.resultList.size());
      for (LotteryDrawResult other_element : other.resultList) {
        __this__resultList.add(new LotteryDrawResult(other_element));
      }
      this.resultList = __this__resultList;
    }
  }

  public DrawResultData deepCopy() {
    return new DrawResultData(this);
  }

  @Override
  public void clear() {
    setCurrentPageIsSet(false);
    this.currentPage = 0;
    setPageSizeIsSet(false);
    this.pageSize = 0;
    setTotalSizeIsSet(false);
    this.totalSize = 0;
    this.resultList = null;
  }

  public int getCurrentPage() {
    return this.currentPage;
  }

  public DrawResultData setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
    setCurrentPageIsSet(true);
    return this;
  }

  public void unsetCurrentPage() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __CURRENTPAGE_ISSET_ID);
  }

  /** Returns true if field currentPage is set (has been assigned a value) and false otherwise */
  public boolean isSetCurrentPage() {
    return EncodingUtils.testBit(__isset_bitfield, __CURRENTPAGE_ISSET_ID);
  }

  public void setCurrentPageIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __CURRENTPAGE_ISSET_ID, value);
  }

  public int getPageSize() {
    return this.pageSize;
  }

  public DrawResultData setPageSize(int pageSize) {
    this.pageSize = pageSize;
    setPageSizeIsSet(true);
    return this;
  }

  public void unsetPageSize() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __PAGESIZE_ISSET_ID);
  }

  /** Returns true if field pageSize is set (has been assigned a value) and false otherwise */
  public boolean isSetPageSize() {
    return EncodingUtils.testBit(__isset_bitfield, __PAGESIZE_ISSET_ID);
  }

  public void setPageSizeIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __PAGESIZE_ISSET_ID, value);
  }

  public int getTotalSize() {
    return this.totalSize;
  }

  public DrawResultData setTotalSize(int totalSize) {
    this.totalSize = totalSize;
    setTotalSizeIsSet(true);
    return this;
  }

  public void unsetTotalSize() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __TOTALSIZE_ISSET_ID);
  }

  /** Returns true if field totalSize is set (has been assigned a value) and false otherwise */
  public boolean isSetTotalSize() {
    return EncodingUtils.testBit(__isset_bitfield, __TOTALSIZE_ISSET_ID);
  }

  public void setTotalSizeIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __TOTALSIZE_ISSET_ID, value);
  }

  public int getResultListSize() {
    return (this.resultList == null) ? 0 : this.resultList.size();
  }

  public java.util.Iterator<LotteryDrawResult> getResultListIterator() {
    return (this.resultList == null) ? null : this.resultList.iterator();
  }

  public void addToResultList(LotteryDrawResult elem) {
    if (this.resultList == null) {
      this.resultList = new ArrayList<LotteryDrawResult>();
    }
    this.resultList.add(elem);
  }

  public List<LotteryDrawResult> getResultList() {
    return this.resultList;
  }

  public DrawResultData setResultList(List<LotteryDrawResult> resultList) {
    this.resultList = resultList;
    return this;
  }

  public void unsetResultList() {
    this.resultList = null;
  }

  /** Returns true if field resultList is set (has been assigned a value) and false otherwise */
  public boolean isSetResultList() {
    return this.resultList != null;
  }

  public void setResultListIsSet(boolean value) {
    if (!value) {
      this.resultList = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case CURRENT_PAGE:
      if (value == null) {
        unsetCurrentPage();
      } else {
        setCurrentPage((Integer)value);
      }
      break;

    case PAGE_SIZE:
      if (value == null) {
        unsetPageSize();
      } else {
        setPageSize((Integer)value);
      }
      break;

    case TOTAL_SIZE:
      if (value == null) {
        unsetTotalSize();
      } else {
        setTotalSize((Integer)value);
      }
      break;

    case RESULT_LIST:
      if (value == null) {
        unsetResultList();
      } else {
        setResultList((List<LotteryDrawResult>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case CURRENT_PAGE:
      return Integer.valueOf(getCurrentPage());

    case PAGE_SIZE:
      return Integer.valueOf(getPageSize());

    case TOTAL_SIZE:
      return Integer.valueOf(getTotalSize());

    case RESULT_LIST:
      return getResultList();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case CURRENT_PAGE:
      return isSetCurrentPage();
    case PAGE_SIZE:
      return isSetPageSize();
    case TOTAL_SIZE:
      return isSetTotalSize();
    case RESULT_LIST:
      return isSetResultList();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof DrawResultData)
      return this.equals((DrawResultData)that);
    return false;
  }

  public boolean equals(DrawResultData that) {
    if (that == null)
      return false;

    boolean this_present_currentPage = true;
    boolean that_present_currentPage = true;
    if (this_present_currentPage || that_present_currentPage) {
      if (!(this_present_currentPage && that_present_currentPage))
        return false;
      if (this.currentPage != that.currentPage)
        return false;
    }

    boolean this_present_pageSize = true;
    boolean that_present_pageSize = true;
    if (this_present_pageSize || that_present_pageSize) {
      if (!(this_present_pageSize && that_present_pageSize))
        return false;
      if (this.pageSize != that.pageSize)
        return false;
    }

    boolean this_present_totalSize = true;
    boolean that_present_totalSize = true;
    if (this_present_totalSize || that_present_totalSize) {
      if (!(this_present_totalSize && that_present_totalSize))
        return false;
      if (this.totalSize != that.totalSize)
        return false;
    }

    boolean this_present_resultList = true && this.isSetResultList();
    boolean that_present_resultList = true && that.isSetResultList();
    if (this_present_resultList || that_present_resultList) {
      if (!(this_present_resultList && that_present_resultList))
        return false;
      if (!this.resultList.equals(that.resultList))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(DrawResultData other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetCurrentPage()).compareTo(other.isSetCurrentPage());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCurrentPage()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.currentPage, other.currentPage);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPageSize()).compareTo(other.isSetPageSize());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPageSize()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.pageSize, other.pageSize);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetTotalSize()).compareTo(other.isSetTotalSize());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTotalSize()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.totalSize, other.totalSize);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetResultList()).compareTo(other.isSetResultList());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetResultList()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.resultList, other.resultList);
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
    StringBuilder sb = new StringBuilder("DrawResultData(");
    boolean first = true;

    sb.append("currentPage:");
    sb.append(this.currentPage);
    first = false;
    if (!first) sb.append(", ");
    sb.append("pageSize:");
    sb.append(this.pageSize);
    first = false;
    if (!first) sb.append(", ");
    sb.append("totalSize:");
    sb.append(this.totalSize);
    first = false;
    if (!first) sb.append(", ");
    sb.append("resultList:");
    if (this.resultList == null) {
      sb.append("null");
    } else {
      sb.append(this.resultList);
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

  private static class DrawResultDataStandardSchemeFactory implements SchemeFactory {
    public DrawResultDataStandardScheme getScheme() {
      return new DrawResultDataStandardScheme();
    }
  }

  private static class DrawResultDataStandardScheme extends StandardScheme<DrawResultData> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, DrawResultData struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // CURRENT_PAGE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.currentPage = iprot.readI32();
              struct.setCurrentPageIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // PAGE_SIZE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.pageSize = iprot.readI32();
              struct.setPageSizeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // TOTAL_SIZE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.totalSize = iprot.readI32();
              struct.setTotalSizeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // RESULT_LIST
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list16 = iprot.readListBegin();
                struct.resultList = new ArrayList<LotteryDrawResult>(_list16.size);
                for (int _i17 = 0; _i17 < _list16.size; ++_i17)
                {
                  LotteryDrawResult _elem18;
                  _elem18 = new LotteryDrawResult();
                  _elem18.read(iprot);
                  struct.resultList.add(_elem18);
                }
                iprot.readListEnd();
              }
              struct.setResultListIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, DrawResultData struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(CURRENT_PAGE_FIELD_DESC);
      oprot.writeI32(struct.currentPage);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(PAGE_SIZE_FIELD_DESC);
      oprot.writeI32(struct.pageSize);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(TOTAL_SIZE_FIELD_DESC);
      oprot.writeI32(struct.totalSize);
      oprot.writeFieldEnd();
      if (struct.resultList != null) {
        oprot.writeFieldBegin(RESULT_LIST_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.resultList.size()));
          for (LotteryDrawResult _iter19 : struct.resultList)
          {
            _iter19.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class DrawResultDataTupleSchemeFactory implements SchemeFactory {
    public DrawResultDataTupleScheme getScheme() {
      return new DrawResultDataTupleScheme();
    }
  }

  private static class DrawResultDataTupleScheme extends TupleScheme<DrawResultData> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, DrawResultData struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetCurrentPage()) {
        optionals.set(0);
      }
      if (struct.isSetPageSize()) {
        optionals.set(1);
      }
      if (struct.isSetTotalSize()) {
        optionals.set(2);
      }
      if (struct.isSetResultList()) {
        optionals.set(3);
      }
      oprot.writeBitSet(optionals, 4);
      if (struct.isSetCurrentPage()) {
        oprot.writeI32(struct.currentPage);
      }
      if (struct.isSetPageSize()) {
        oprot.writeI32(struct.pageSize);
      }
      if (struct.isSetTotalSize()) {
        oprot.writeI32(struct.totalSize);
      }
      if (struct.isSetResultList()) {
        {
          oprot.writeI32(struct.resultList.size());
          for (LotteryDrawResult _iter20 : struct.resultList)
          {
            _iter20.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, DrawResultData struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(4);
      if (incoming.get(0)) {
        struct.currentPage = iprot.readI32();
        struct.setCurrentPageIsSet(true);
      }
      if (incoming.get(1)) {
        struct.pageSize = iprot.readI32();
        struct.setPageSizeIsSet(true);
      }
      if (incoming.get(2)) {
        struct.totalSize = iprot.readI32();
        struct.setTotalSizeIsSet(true);
      }
      if (incoming.get(3)) {
        {
          org.apache.thrift.protocol.TList _list21 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.resultList = new ArrayList<LotteryDrawResult>(_list21.size);
          for (int _i22 = 0; _i22 < _list21.size; ++_i22)
          {
            LotteryDrawResult _elem23;
            _elem23 = new LotteryDrawResult();
            _elem23.read(iprot);
            struct.resultList.add(_elem23);
          }
        }
        struct.setResultListIsSet(true);
      }
    }
  }

}


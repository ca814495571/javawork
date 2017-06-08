namespace java com.cqfc.protocol.idgenerate

struct IdGenerate{
    1:string idName,	//ID名称
    2:i64 currentId,	//当前ID
    3:i32 offset,	//偏移量
    4:string lastUpdateTime	//最后更新时间
}

service IdGenerateService {
    //根据ID名称生成ID
    i64 idGen(1:string idName);
}
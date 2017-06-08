namespace java com.cqfc.samplemodule.protocol

struct User {
    1:i32 id,
    2:string name,
    3:i32 age,
    4:string address
}

service SampleService {
    list<User> getUserList(),
    User getUserById(1:i32 id)
}
namespace java com.thrift.user

struct UserInfo {
    1: i32 id;
    2: string username;
    3: string password;
    4: string realName;
    5: string mobile;
    6: string email;
    7: string introduce
    8: i32 stars
}

service UserService {
    UserInfo getUserById(i32 id);

    UserInfo getUserByName(string username);

    UserInfo getTeacherById(i32 id);

    void registerUser(UserInfo userinfo);
}
package main.java;

import java.sql.*;
import java.util.*;

class SqlContorller {
    private String url;
    private String user;
    private String password;

    SqlContorller(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }
}


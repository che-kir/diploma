/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spectrex;

/**
 *
 * @author KirillUser
 */
abstract class User {
    String login;
    String password;
    User(String login, String password)
    {
        this.login = login;
        this.password = password;
    }
    User() {}
    
}

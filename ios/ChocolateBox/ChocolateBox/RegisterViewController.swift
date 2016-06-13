//
//  RegisterViewController.swift
//  ChocolateBox
//
//  Created by Tim Kim on 6/7/16.
//  Copyright © 2016 259Williams. All rights reserved.
//

import UIKit
import Parse

class RegisterViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        passwordTextField.secureTextEntry = true
        verifyTextField.secureTextEntry = true
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBOutlet var fullnameTextField : UITextField!
    @IBOutlet var usernameTextField : UITextField!
    @IBOutlet var emailTextField : UITextField!
    @IBOutlet var passwordTextField : UITextField!
    @IBOutlet var verifyTextField : UITextField!
    @IBOutlet var messageLabel: UITextView!
    
    @IBAction func registerButton(sender : AnyObject){
        let fullname = fullnameTextField.text
        let username = usernameTextField.text
        let email = emailTextField.text
        let password = passwordTextField.text
        
        addUser(fullname!, regUser: username!, regEmail: email!, regPassword: password!)
    /*
        if (fullname == ""){
            messageLabel.text = "Please input a valid name."
        }
        else if(username == ""){
            messageLabel.text = "Please input a valid username."
        }
        else if(email == ""){
            messageLabel.text = "Please input a valid email."
        }
        else if(password == ""){
            messageLabel.text = "Please input a valid password."
        }
        else if(password != verifyTextField.text){
            messageLabel.text = "Your password and verification do not match."
        }
        else{
            addUser(fullname!, regUser: username!, regEmail: email!, regPassword: password!)
        }
    */
    }

    func addUser(regName: String, regUser: String, regEmail: String, regPassword: String){
        let user = PFUser()
        user.username = regUser
        user.password = regPassword
        user.signUpInBackgroundWithBlock { (success: Bool, error: NSError?)  -> Void in
            if success {
                self.messageLabel.text = "You are now signed up!";
                self.performSegueWithIdentifier("RegisterSegue", sender: nil)
                print("User Uploaded")
            } else {
                print("Error: \(error)")
            }
        }
        let match = PFObject(className: "Matching")
        match.setObject(regName, forKey: "fullname")
        match.setObject(regUser, forKey: "username")
        match.setObject(regEmail, forKey: "emailAddress")
        match.setObject(false, forKey: "hasMatched")
        match.setObject("", forKey: "matchedUser")
        match.saveInBackgroundWithBlock { (succeeded, error) -> Void in
            if succeeded {
                print("Match Uploaded")
            } else {
                print("Error: \(error)")
            }
        }

    }
    
}




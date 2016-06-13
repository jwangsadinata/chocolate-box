//
//  LoginViewController.swift
//  ChocolateBox
//
//  Created by Tim Kim on 6/7/16.
//  Copyright © 2016 259Williams. All rights reserved.
//

import UIKit
import Parse

class LoginViewController: UIViewController {
    
    @IBOutlet var usernameTextField : UITextField!
    @IBOutlet var passwordTextField : UITextField!
    @IBOutlet var messageLabel : UITextView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        passwordTextField.secureTextEntry = true
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func shouldPerformSegueWithIdentifier(identifier: String, sender: AnyObject?) -> Bool {
        var value: Bool = true
        if identifier == "VerificationSegue" { // you define it in the storyboard (click on the segue, then Attributes' inspector > Identifier
            if usernameTextField.text!.isEmpty{
                value = false
                messageLabel.text = "Please input a valid username"
            }
            else if passwordTextField.text!.isEmpty{
                value = false
                messageLabel.text = "Please input a valid password"
            }
            else{
                do {
                    let currUser = try PFUser.logInWithUsername(usernameTextField.text!, password: passwordTextField.text!)
                    //performing synchronous login
                    if currUser.username == nil {
                        value = false
                        messageLabel.text = "Incorrect email/password"
                        print ("nothing should happen...")
                    } else{
                        print ("login!")
                    }
                }
                catch {
                    value = false
                    print ("Something went wrong")
                }
            }

            if (value == false){
            //if (self.usernameTextField.text!.isEmpty == true) {
                print("*** NOPE, segue wont occur")
                return false
            }
            else {
                print("*** YEP, segue will occur")
            }
        }
        
        // by default, transition
        return true
    }
    
    @IBAction func loginButton(sender : AnyObject){
        self.shouldPerformSegueWithIdentifier("VerificationSegue", sender: nil)
        //adding a conditional to the segue
    }
/*
    override func shouldPerformSegueWithIdentifier(identifier: String, sender: AnyObject?) -> Bool {
        var value : Bool = true
        if identifier == "VerificationSegue" { // you define it in the storyboard (click on the segue, then Attributes' inspector > Identifier
            
            PFUser.logInWithUsernameInBackground(usernameTextField.text!, password: passwordTextField.text!){ user, error in
                    if (user == nil) {
                        value = false
                        print(user)
                        print("*** NOPE, segue wont occur")
                        return
                    }
                    else {
                        print("*** YEP, segue will occur")
                    }
                }
            
        }
    
        return value
        // by default, transition
    }
    
    @IBAction func loginButton(sender : AnyObject){
        self.shouldPerformSegueWithIdentifier("VerificationSegue", sender: nil)
    }

     PFUser.logInWithUsernameInBackground(usernameTextField.text!, password: passwordTextField.text!) { user, error in
     if user != nil {
     self.shouldPerformSegueWithIdentifier("VerificationSegue", sender: nil)
     } else if let error = error {
     print("Error: \(error)")
     }
     }
 */
    
    
}

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
    
    @IBAction func loginButton(sender : AnyObject){
        PFUser.logInWithUsernameInBackground(usernameTextField.text!, password: passwordTextField.text!) { user, error in
            if user != nil {
                self.performSegueWithIdentifier("VerificationSegue", sender: nil)
            } else if let error = error {
                print("Error: \(error)")            }
        }
    }

}

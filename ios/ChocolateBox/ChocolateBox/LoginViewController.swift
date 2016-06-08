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

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBOutlet var usernameTextField : UITextField!
    @IBOutlet var passwordTextField : UITextField!
    @IBOutlet var messageLabel : UITextView!
    
    @IBAction func loginButton(sender : AnyObject){
        let username = usernameTextField.text
        let password = passwordTextField.text
        if (username != nil || password != nil) {
            self.messageLabel.text = "You have logged in";
            self.performSegueWithIdentifier("VerificationSegue", sender: self)
        } else {
            self.messageLabel.text = "You are not registered";
        }
    }

}

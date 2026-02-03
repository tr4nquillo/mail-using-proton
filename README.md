# Send Mail Using Proton

## Purpose

The purpose of this tiny project is to find out how to send emails from an application running in a container using existing email subscription, in my case this is Proton. This solved the following business requirements:
    - sending out marketing emails or secure emails for confirming subscriptions/resetting access to a small group of people on a regular basis (according to Proton, Proton is not designed to be used for mass email campaigns)
    -  saving money for an additional mail service subscription by utilizing existing email subscription 

## How does it work?

A plain Java application that uses jakarta mail library to set up a connection to the SMTP service and send a message. Username and password are passed to the application as environment variables. The information on what connection parameters to set and how to generate a token can be found [here](https://proton.me/support/smtp-submission#setup). 

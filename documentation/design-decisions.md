# Design decisions

## Configuring the mail subscription in the container

There are two options for doing that:
- starting the [Proton Mail Bridge](https://proton.me/mail/bridge) application in a container and sending the emails
- using the [SMTP Submission](https://proton.me/support/smtp-submission)

The first option works really well locally, but running the bridge in a container requires much more effort - setting up a keychain, configuring the mail bridge with a username and password and downloading the connection parameters must be done by passing arguments to the bridge --cli through the command line. Because of this, I opted for the SMTP submission.
# active-notification-framework
The active-notification-framework is a framework managing notifications on multiple registered devices (phone,watch,...) based on activity, location and behaviour. 

Notifications will get different signals depending on their importance and the context of the user. In the first versions the channels phone and watch are used to create a better user experience for a user with notifications.

The framework is a result of a master's thesis that was made for the university of applied science in Offenburg and the Fiducia & GAD IT AG.

## Development status and further development
This is the first Version. The current status of the code corresponds to the status immediately after the master's thesis. In the next iterations the project will be made more productive and easy to use. 

At the moment android smartphones and android wear smart watches are supported.

## Requirements

You need an android device with sdk version 21 or higher. 

This frameworks uses the google maps api for location awareness and address -> coordinate mapping. 
You need to add an "google.android.geo.API_KEY" to the AndroidManifest.xml of your app. 
Google provides the information how to obtain the key and how to insert it to your project here: <https://developers.google.com/maps/documentation/android-api/signup?hl=de>


## How to Use

The simple usage of the framework after the steps explained in the Requirements chapter can be viewed in the file "MainActivity" in the forlder sample. In this
class the sample usage of the framework in an small activity is shown. If there is an interest for the sample app please contact Felix Schiefer. If there are Questions for
more informations or samples please contact Felix to. 

## Contact

Please contact Felix.Schiefer_AnF@gmx.de or Jens.Zimmermann@fiduciagad.de

###### Disclaimer
The Fiducia & GAD IT AG  is the service provider for information technology of the cooperative financial network. The provided software has emerged as part of a thesis within the Fiducia & GAD IT AG . It's now available as open source in this repository. The development of the software is carried out in deviation from the company's internal development process. It is therefore provided only WITHOUT ANY WARRANTY 



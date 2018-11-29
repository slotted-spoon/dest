# README

This app is a mock-up for a User Experience Design course at Olin College of Engineering. It is a looks-like-acts-like prototype for navigating an indoor space.

## Set-Up

To make this app work, build it on your Android phone and connect an PS4 controller via BlueTooth.

## Running the App

Open IndoorNav on your Android.

### EnterDest

The user should select a destination from the first screen and hit the arrow. Currently, "AC 428 - SEM Room" is the matching room for the selection. A list of locations will show when the user begins to type into the Destination textbox. Hitting the arrow will take them to Route Options.

### Route Options

From this screen, they can swipe back to edit the destination or hit the edit button. Other options include Elevator Route or Stair Route, with the default on stairs. These bottom tabs change the instructions during navigation. "Pocket Mode" enables haptic feedback. Once the user is satisfied with the options selected, they can hit 'go'.

### Navigation

After hitting the 'go' button, they will enter navigation mode. From here, the PS4 controller will act as the automation. The top left trigger button will cause a 'heartbeat pulse' as long as the vibrate setting remains. The bottom right trigger progresses the screen with a single long buzz (cancelling the heartbeat). The holding down the bottom left trigger will create an angry buzzing sound.

The user can swipe through pictures in order to preview next steps. Progressing the navigation with the PS4 controller will return them to the step they were on before previewing. The user can also drag up a list view of instructions from the bottom of the screen. Clicking on any instruction will scroll them to that picture and instruction.

At any point, hitting the 'End Navigation' button will cancel navigation.

### Arrived

Once the user has arrived, they can click the 'Finish' button that will return them to the original landing screen.

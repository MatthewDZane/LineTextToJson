# Line Text to Json

## *"File Format Converter"*

# Description:
    Converts a text file, containing chat logs, as outputted by the Line app, into a json format. The main purpose for creating this program was to convert Line text files in json files for the Entanglement project. (see https://github.com/MatthewDZane/Entanglement). For some types of text messages, like Photos, Videos, or Links, the actual message content is removed in order to not confuse the Chat Analyzer.

# How to Use Line Text to Json

| Step 0: Downloading your Line messages

  On Mobile

  | Step 0.1: Go to your desired chat and press the info button at the top right

<img src=./Examples/InfoButton.jpg />

  | Step 0.2: Press the "Other settings" button toward the bottom

<img src=./Examples/OtherSettingsButton.png />

  | Step 0.3: Press "Export chat history" and choose where you want to save the file

<img src=./Examples/ExportChatHistory.png />

  On Desktop

  | Step 0.1: Go to you desired chat and click the info button at the top right

<img src=./Examples/DesktopInfoButton.png >/

  | Step 0.2: Click "Save chat" and choose where you want to save the file

<img src=./Examples/SaveChat.png />

| Step 1: Move desired Line text file to the LineTextToJson/src/InputFiles/ directory

| Step 2: Using a bash shell, navigate to the LineTextToJson directory
          Ex: "cd pathToProject/LineTextToJson"

| Step 3: run "./convertLineToJson.sh 'Name of Line chat file' 'Your name in Line'"
          Note: Replace text in the single quotes with desired names, but actually
                use the single quotes.

| Step 4: Access converted json file in the LineTextToJson/bin/OutputFiles directory

# Now you have converted your Line text file to a json format! Thank you for
# using this project!




 

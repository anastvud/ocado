Hello! Thank you for interesting task.

I assumed that config.json file will be in src/main/resources/config.json.

Main function creates new BasketSplitter object with absolute path file that should be changed in code. The same path should be changed in BasketSplitterTest class and also added path for file with incorrect json, by writing "/badjson.json" insted of "/config.json".

The example that was stated in the task is present as List of Strings created in the main method. There is also simple logging to see the output.

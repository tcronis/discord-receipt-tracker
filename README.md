# discord-receipt-tracker
A small quick and dirty receipt tracker that will use Discord's private messages to store receipt data. 
The purpose was to have a single storage place for receipts and receipt data without having to create a REST API. 
This is by no means finished and the functionality it can do now is very limited.

## Functionality ##
 - For storing data, you can interact with the bot by starting to store a receipt with the command '!start' or '!create receipt'.
    - The name of the store is required first
        - It can be in any format, but misspellings aren't checked so Walmart and Walmrt aren't considered to be the same thing when running the statistical analysis. 
    - Next, the date is requested, just enter in the amount without any extra symbols
        - I.E 77.89 not $ 77.98
            - There is no input validation really, so you will cause an error when throwing off the inputs
    - The category is requested next
        - As of right now, there are just 6 categories which are all hardcoded
            - in a later update, I will create dynamic categories that you can set
    - Finally, an image is asked for, which is optional
        - Either attach a photo or reply with 'No' to skip the photo
        - There is no way as of right now to query for an image, but this will be added in, in a later update
- Stats
    - The only stats the bot can do now is to run the totals for every store and category for a given month. 
        - This is where the misspellings and slight deviations of spellings can cause inaccuracies. Since QT and QuickTrip are seen as two different stores.

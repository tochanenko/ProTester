import {Action} from "./actions/action.model";

export const ACTIONS : Action[]= [
  {
    "id":1,
    "type": "TECHNICAL",
    "name": "ClickOnElementWithId",
    "description": "Click on element with specified id",
    "preparedParams":null
  },
  { "id":2,
    "type": "TECHNICAL",
    "name": "ClickOnElementWithXPath",
    "description": "Click on element with specified xpath",
    "preparedParams":null
  },
  {
    "id":3,
    "type": "TECHNICAL",
    "name": "InputTextIntoFieldWithId",
    "description": "Input specified text into field with specified id",
    "preparedParams":null
  },
  {
    "id":4,
    "type": "REST",
    "name": "GoToUrl",
    "description": "Performs get method on specified url",
    "preparedParams": null
  },
  {
    "id":5,
    "type": "TECHNICAL",
    "name": "ClickOnLinkWithText",
    "description": "Click on link with specified text",
    "preparedParams":null
  },
  {
    "id":6,
    "type": "TECHNICAL",
    "name": "InputTextIntoFieldWithXPath",
    "description": "Input specified text into field with specified xpath",
    "preparedParams":null
  },

]

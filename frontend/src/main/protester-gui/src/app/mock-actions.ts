import {Action} from "./models/action.model";

export const ACTIONS : Action[]= [
  {
    "id": 3,
    "type": "TECHNICAL",
    "name": "ClickOnElementWithId",
    "description": "Click on element with specified id",
    "parameterNames": [
      "id"
    ]
  },
  {
    "id": 6,
    "type": "TECHNICAL",
    "name": "ClickOnElementWithXPath",
    "description": "Click on element with specified xpath",
    "parameterNames": [
      "xpath"
    ]
  },
  {
    "id": 1,
    "type": "TECHNICAL",
    "name": "InputTextIntoFieldWithId",
    "description": "Input specified text into field with specified id",
    "parameterNames": [
      "text",
      "id"
    ]
  },
  {
    "id": 5,
    "type": "REST",
    "name": "GoToUrl",
    "description": "Performs get method on specified url",
    "parameterNames": [
      "url"
    ]
  },
  {
    "id": 2,
    "type": "TECHNICAL",
    "name": "ClickOnLinkWithText",
    "description": "Click on link with specified text",
    "parameterNames": [
      "text"
    ]
  },
  {
    "id": 4,
    "type": "TECHNICAL",
    "name": "InputTextIntoFieldWithXPath",
    "description": "Input specified text into field with specified xpath",
    "parameterNames": [
      "text",
      "xpath"
    ]
  },

]

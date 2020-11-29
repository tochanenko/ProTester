import {Action} from "./models/action.model";

export const ACTIONS : Action[]= [
  {
    "id":null,
    "declarationId": 3,
    "type": "TECHNICAL",
    "name": "ClickOnElementWithId",
    "description": "Click on element with specified id",
    "parameterNames": [
      "id"
    ],
    "preparedParams":null
  },
  { "id":null,
    "declarationId": 6,
    "type": "TECHNICAL",
    "name": "ClickOnElementWithXPath",
    "description": "Click on element with specified xpath",
    "parameterNames": [
      "xpath"
    ],
    "preparedParams":null
  },
  {
    "id":null,
    "declarationId": 1,
    "type": "TECHNICAL",
    "name": "InputTextIntoFieldWithId",
    "description": "Input specified text into field with specified id",
    "parameterNames": [
      "text",
      "id"
    ],
    "preparedParams":null
  },
  {
    "id":null,
    "declarationId": 5,
    "type": "REST",
    "name": "GoToUrl",
    "description": "Performs get method on specified url",
    "parameterNames": [
      "url"
    ],
    "preparedParams": null
  },
  {
    "id":null,
    "declarationId": 2,
    "type": "TECHNICAL",
    "name": "ClickOnLinkWithText",
    "description": "Click on link with specified text",
    "parameterNames": [
      "text"
    ],
    "preparedParams":null
  },
  {
    "id":null,
    "declarationId": 4,
    "type": "TECHNICAL",
    "name": "InputTextIntoFieldWithXPath",
    "description": "Input specified text into field with specified xpath",
    "parameterNames": [
      "text",
      "xpath"
    ],
    "preparedParams":null
  },

]

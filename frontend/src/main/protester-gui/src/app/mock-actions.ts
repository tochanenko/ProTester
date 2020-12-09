import {Action} from "./actions/action.model";

export const ACTIONS : Action[]= [
    {
      "id": 1,
      "name": "InputTextIntoFieldWithIdAction",
      "description": "Input specified text into field with specified id",
      "type": "TECHNICAL",
      "parameterNames": [
        "text",
        "id"
      ],
      "preparedParams": null,
    },
    {
      "id": 2,
      "name": "GoToUrlAction",
      "description": "Performs get method on specified url",
      "type": "REST",
      "parameterNames": [
        "url"
      ],
      "preparedParams": null,
    },
    {
      "id": 3,
      "name": "ClickOnElementWithXPathAction",
      "description": "Click on element with specified xpath",
      "type": "TECHNICAL",
      "parameterNames": [
        "xpath"
      ],
      "preparedParams": null,
    },
    {
      "id": 4,
      "name": "ClickOnLinkWithTextAction",
      "description": "Click on link with specified text",
      "type": "TECHNICAL",
      "parameterNames": [
        "text"
      ],
      "preparedParams": null,
    },
    {
      "id": 5,
      "name": "ClickOnElementWithIdAction",
      "description": "Click on element with specified id",
      "type": "TECHNICAL",
      "parameterNames": [
        "id"
      ],
      "preparedParams": null,
    },
    {
      "id": 6,
      "name": "InputTextIntoFieldWithXPathAction",
      "description": "Input specified text into field with specified xpath",
      "type": "TECHNICAL",
      "parameterNames": [
        "text",
        "xpath"
      ],
      "preparedParams": null,
    }
  ]


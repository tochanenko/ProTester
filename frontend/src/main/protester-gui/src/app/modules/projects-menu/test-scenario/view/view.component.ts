import {Component, OnInit} from '@angular/core';
import {FormBuilder} from "@angular/forms";
import {Subscription} from "rxjs";
import {ActivatedRoute, Router} from "@angular/router";
import {TestScenarioService} from "../../../../services/test-scenario/test-scenario-service";
import {TestScenario} from "../../../../models/test-scenario";

@Component({
  selector: 'app-scenario-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.css']
})

export class ViewComponent implements OnInit {
  validatorsConfig = {
    name: {
      minLength: 5,
      maxLength: 15
    },
    description: {
      maxLength: 200
    }
  }

  private subscription: Subscription;
  scenario_id: number;
  scenario: TestScenario;

  components: TestScenario[] = [];
  step_id: number = 1;
  componentCtx = {
    components: this.components
  }

  temporaryParams = {};

  constructor(
    private formBuilder: FormBuilder,
    private scenarioService: TestScenarioService,
    private router: Router,
    private activateRoute: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    this.getIdFromParams();
    this.getScenarioById(this.scenario_id);
  }


  getIdFromParams(): void {
    this.subscription = this.activateRoute.params.subscribe(params=>this.scenario_id=params['id']);
  }

  getScenarioById(id: number): void {
    this.scenarioService.getById(id).subscribe(scenario => {

      let parentParams = {};
      scenario.parameterNames.forEach(param => {
        parentParams[param] = "${" + param + "}";
      });

      if (scenario.steps) {
        let mappingParams = {};
        this.recursiveStepParsing(scenario.steps, mappingParams, parentParams);
      }

      scenario.description = this.parseDescription(scenario.description.toString());
      this.scenario = scenario;
      this.componentCtx.components = scenario.steps;
    })
  }

  getComponentsOfSteps(steps) {
    return {array: steps.map(step => step.component)};
  }

  checkIfParamInterpolated(param: string) {
    if (typeof param !== "undefined" || param !== null) {
      const regex = '${';
      return param.includes(regex);
    }
    else {
      return;
    }
  }

  cleanParam(param: string) {
    let new_param = param.slice(2, param.length - 1);
    return new_param;
  }

  parseDescription(description: string) {
    const regexp = new RegExp('(\\${\\w*})');
    let splitted = description.split(regexp);
    return splitted.map(sub_string => {
      if (sub_string.includes("${")) {
        return {
          text: sub_string.replace('${', '').replace('}', ''),
          input: true
        }
      } else {
        return {
          text: sub_string,
          input: false
        }
      }
    })
  }

  // "id": "link_id"
  recursiveStepParsing(steps, mappingParams, parentParams) {
    steps.forEach(item => {
      let clonedMappingParams = Object.assign({}, mappingParams);
      item.component.description = this.parseDescription(item.component.description);
      if (Object.keys(item.parameters).length > 0) {
        for (let [key, value] of Object.entries(item.parameters)) {
          let cleanedParam = this.cleanParam(value.toString());
          if (parentParams[cleanedParam]) {
            item.parameters[key] = parentParams[cleanedParam];
            parentParams[key] = parentParams[cleanedParam];
          }
          if (!this.checkIfParamInterpolated(value.toString())) {
            clonedMappingParams[key] = value;
          } else {
            let param = this.cleanParam(value.toString());
            if (clonedMappingParams[param]) {
              item.parameters[key] = clonedMappingParams[param]
              clonedMappingParams[key] = clonedMappingParams[param];
            }
          }
        }
      }
      if (item.component.steps) {
        this.recursiveStepParsing(item.component.steps, clonedMappingParams, parentParams);
      }
    })
  }


  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}

import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Subscription} from "rxjs";
import {Step} from "../../../../models/step.model";
import {ActivatedRoute, Router} from "@angular/router";
import {CompoundManageService} from "../../../../services/compound-manage.service";
import {OuterComponent} from "../../../../models/outer.model";

@Component({
  selector: 'app-compound-view',
  templateUrl: './compound-view.component.html',
  styleUrls: ['./compound-view.component.css']
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
  compoundCreateForm: FormGroup;
  compound_id: number;
  compound: OuterComponent;

  components: Step[] = [];
  step_id: number = 1;
  componentCtx = {
    components: this.components
  }

  temporaryParams = {};

  constructor(
    private formBuilder: FormBuilder,
    private compoundService: CompoundManageService,
    private router: Router,
    private activateRoute: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    this.getIdFromParams();
    this.getCompoundById(this.compound_id);
  }


  getIdFromParams(): void {
    this.subscription = this.activateRoute.queryParams.subscribe(params=>this.compound_id=params['id']);
  }

  getCompoundById(id: number): void {
    this.compoundService.getCompoundById(id).subscribe(compound => {

      let parentParams = {};
      compound.parameterNames.forEach(param => {
        parentParams[param] = "${" + param + "}";
      });

      if (compound.steps) {
        let mappingParams = {};
        this.recursiveStepParsing(compound.steps, mappingParams, parentParams);
      }

      compound.name = this.parseDescription(compound.name.toString());
      this.compound = compound;
      this.componentCtx.components = compound.steps;
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

  get formControls() {
    return this.compoundCreateForm.controls;
  }

  // "id": "link_id"
  recursiveStepParsing(steps, mappingParams, parentParams) {
    steps.forEach(item => {
      let clonedMappingParams = Object.assign({}, mappingParams);
      item.component.name = this.parseDescription(item.component.name);
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

  getParamFieldFromComponent(param, id) {
    return this.components.find((step) => {
      if (step.id === id) {
        return step.parameters[param];
      }
    })
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}

import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Subscription} from "rxjs";
import {Step} from "../../models/step.model";
import {ActivatedRoute, Router} from "@angular/router";
import {CompoundManageService} from "../../services/compound-manage.service";
import {OuterComponent} from "../../models/outer.model";

@Component({
  selector: 'app-compound-view',
  templateUrl: './compound-view.component.html',
  styleUrls: ['./compound-view.component.css']
})

export class CompoundViewComponent implements OnInit {
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
      this.recursiveStepParsing(compound.steps);
      compound.description = this.parseDescription(compound.description.toString());
      this.compound = compound;
      this.componentCtx.components = compound.steps;
      console.log(this.components)
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


  componentPrepare(component) {
    let step = new Step();
    component.description = this.parseDescription(component.description);

    step.id = this.step_id++;
    step.isAction = component.type !== 'COMPOUND';
    step.component = component;
    step.parameters = new Map<String, String>();

    component.parameterNames.map(param => {
      step.parameters[param] = "";
    });

    component.parameterNames.forEach(param => {
      this.compoundCreateForm.addControl(step.id + '-' + param, new FormControl('', Validators.required));
    });

    if (component.steps) {
      this.recursiveStepParsing(component.steps);
    }

    console.log(component);

    this.components.push(step);

  }


  // "id": "link_id"
  recursiveStepParsing(steps) {
    steps.forEach(item => {
      item.component.description = this.parseDescription(item.component.description);
      if (Object.keys(item.parameters).length > 0) {
        for (let [key, value] of Object.entries(item.parameters)) {
          if (!this.checkIfParamInterpolated(value.toString())) {
            this.temporaryParams[key] = value;
          }
          else {
            let param = this.cleanParam(value.toString());
            if (this.temporaryParams[param]) {
              item.parameters[key] = this.temporaryParams[param]
            }
          }
        }
      }

      if (item.component.steps) {
        this.recursiveStepParsing(item.component.steps);
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

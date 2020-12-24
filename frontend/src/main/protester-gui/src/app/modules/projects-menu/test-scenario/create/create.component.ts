import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {MatBottomSheet} from "@angular/material/bottom-sheet";
import {BottomSheetComponent} from "../../../../components/bottom-sheet/bottom-sheet.component";
import {LibraryBottomsheetInteractionService} from "../../../../services/library/library-bottomsheet-interaction.service";
import {Subscription} from "rxjs";
import {Step} from "../../../../models/step.model";
import {Router} from "@angular/router";
import {CompoundManageService} from "../../../../services/compound-manage.service";
import {StepRepresentation} from "../../../../models/StepRepresentation";
import {TestScenarioService} from "../../../../services/test-scenario/test-scenario-service";

@Component({
  selector: 'app-scenarion-new',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css']
})

export class CreateComponent implements OnInit {
  validatorsConfig = {
    name: {
      minLength: 5,
      maxLength: 60
    },
    description: {
      maxLength: 200
    }
  }

  private actionSubscription: Subscription;
  private compoundSubscription: Subscription;
  scenarioCreateForm: FormGroup;

  components: Step[] = [];
  step_id: number = 1;
  bottomSheetData = {};
  componentCtx = {
    components: this.components
  }
  url = "";
  clickInput = false;

  componentParamsForm: FormGroup;
  scenarioCreateRequest = {};

  constructor(
    private formBuilder: FormBuilder,
    private _bottomSheet: MatBottomSheet,
    private scenarioService: TestScenarioService,
    private compoundService: CompoundManageService,
    private interactionService: LibraryBottomsheetInteractionService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.createForm();
    this.getAllActionsForBottomSheet();
    this.getAllCompoundsForBottomSheet();
    this.updateActionsArray();
    this.updateCompoundsArray();
  }

  createForm(): void {
    this.scenarioCreateForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      description: ['', [Validators.required]]
    })
  }

  getComponentsOfSteps(steps) {
    return {array: steps.map(step => step.component)};
  }

  getStepId(step) {
    return step.component.name + '-' + step.id;
  }

  onFilterKeyboard(event) {
    event.preventDefault();
    event.stopImmediatePropagation();
    event.stopPropagation();
  }

  checkIfParamInterpolated(param: string) {
    if (typeof param !== "undefined" || param !== null) {
      const regex = '${';
      return param.includes(regex);
    } else {
      return;
    }
  }

  cleanParam(param: string) {
    let new_param = param.slice(2, param.length - 1);
    return new_param;
  }

  parseDescription(description: string | Object) {
    if (typeof description !== "object") {
      const regexp = new RegExp('(\\$\\{.+?\\})');
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
    } else {
      return description;
    }
  }

  onSubmit(): void {
    const f = this.formControls;

    if (this.scenarioCreateForm.invalid) {
      console.error(this.scenarioCreateForm, "VAlIDATION ERROR");
      return;
    }

    if (this.components.length === 0) {
      console.error("Set action or compound");
      return;
    }

    this.scenarioCreateRequest['description'] = f.description.value;
    this.scenarioCreateRequest['name'] = f.name.value;
    this.scenarioCreateRequest['steps'] = this.components.map(item => {
      const step: StepRepresentation = {
        id: item.component.id,
        action: item.isAction,
        parameters: item.parameters
      }
      return step;
    });
    this.scenarioService.create(this.scenarioCreateRequest).subscribe((data) => {
        this.router.navigateByUrl('/projects-menu/scenarios').then();
      },
      () => {
        console.error("Error of creation");
      })
  }

  get formControls() {
    return this.scenarioCreateForm.controls;
  }

  updateActionsArray(): void {
    this.actionSubscription = this.interactionService.actionsArrayObserver.subscribe(action => {
      this.componentPrepare(action);
    });
  }

  updateCompoundsArray(): void {
    this.compoundSubscription = this.interactionService.compoundArrayObserver.subscribe(compound => {
      this.componentPrepare(compound);
    });
  }

  componentPrepare(component) {
    let step = new Step();

    step.id = this.step_id++;
    step.isAction = component.type !== 'COMPOUND';
    step.component = component;
    step.parameters = new Map<String, String>();

    component.parameterNames.map(param => {
      step.parameters[param] = '';
    });

    let parentParams = {};
    component.parameterNames.forEach(param => {
      parentParams[param] = "${" + param + "}";
      this.scenarioCreateForm.addControl(step.id + '-' + param, new FormControl('', Validators.required));
    });
    if (component.steps) {
      let mappingParams = {};
      this.recursiveStepParsing(component.steps, mappingParams, parentParams);
    }

    this.components.push(step);

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

  deleteComponentFromArray(components, id): void {
    components.find((component, index, components) => {
      if (component.id === id) {
        components.splice(index, 1);
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

  openBottomSheet(): void {
    this._bottomSheet.open(BottomSheetComponent, {
      data: {components: this.bottomSheetData},
      closeOnNavigation: true
    });
  }

  getAllActionsForBottomSheet(): void {
    this.compoundService.getAllActions().subscribe(data => {
      data['list'].forEach(item => {
        if (typeof item.name !== "object") {
          item.name = this.parseDescription(item.name);
        }
      })
      this.bottomSheetData['actions'] = data['list'];
    });
  }

  getAllCompoundsForBottomSheet(): void {
    this.compoundService.getAllCompounds().subscribe(data => {
      data['list'].forEach(item => {
        if (typeof item.name !== "object") {
          item.name = this.parseDescription(item.name);
        }
      })
      this.bottomSheetData['compounds'] = data['list'];
    });
  }

  ngOnDestroy(): void {
    if (this.actionSubscription) {
      this.actionSubscription.unsubscribe();
    }
    if (this.compoundSubscription) {
      this.compoundSubscription.unsubscribe();
    }
  }

}

import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {MatBottomSheet} from "@angular/material/bottom-sheet";
import {BottomSheetComponent} from "../../../../components/bottom-sheet/bottom-sheet.component";
import {LibraryBottomsheetInteractionService} from "../../../../services/library/library-bottomsheet-interaction.service";
import {Subscription} from "rxjs";
import {Step} from "../../../../models/step.model";
import {ActivatedRoute, Router} from "@angular/router";
import {CompoundManageService} from "../../../../services/compound-manage.service";
import {StepRepresentation} from "../../../../models/StepRepresentation";
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-compound-edit',
  templateUrl: './compound-edit.component.html',
  styleUrls: ['./compound-edit.component.css']
})

export class CompoundEditComponent implements OnInit {
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
  private subscription: Subscription;
  compoundUpdateForm: FormGroup;

  components: Step[] = [];
  step_id: number = 1;
  bottomSheetData = {};
  componentCtx = {
    components: this.components
  }
  url = "";
  clickInput = false;
  compound_id: number;

  componentParamsForm: FormGroup;
  compoundCreateRequest = {};

  constructor(
    private formBuilder: FormBuilder,
    private _bottomSheet: MatBottomSheet,
    private compoundService: CompoundManageService,
    private interactionService: LibraryBottomsheetInteractionService,
    private router: Router,
    private activateRoute: ActivatedRoute

  ) {
  }

  ngOnInit(): void {
    this.createForm();
    this.getIdFromParams();
    this.getCompoundById(this.compound_id);
    this.getAllActionsForBottomSheet();
    this.getAllCompoundsForBottomSheet();
    this.updateActionsArray();
    this.updateCompoundsArray();
  }

  createForm(): void {
    this.compoundUpdateForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(this.validatorsConfig.name.minLength)]],
      description: ['', [Validators.required]]
    })
  }


  getIdFromParams(): void {
    this.subscription = this.activateRoute.params.subscribe(params => this.compound_id=params['id']);
  }

  getCompoundById(id: number): void {
    this.compoundService.getCompoundById(id).subscribe(compound => {
      console.log(compound);
      let f = this.formControls;
      f.name.setValue(compound.name);
      f.description.setValue(compound.description);

      let parentParams = {};
      compound.parameterNames.forEach(param => {
        parentParams[param] = "${" + param + "}";
      });

      if (compound.steps) {
        let mappingParams = {};
        this.recursiveStepParsing(compound.steps, mappingParams, parentParams);
      }

      compound.steps.forEach(component => {
        this.componentPrepare(component.component, component.parameters);
      })

    })
  }

  getComponentsOfSteps(steps) {
    return {array: steps.map(step => step.component)};
  }

  getStepId(step) {
    return step.component.name + '-' + step.id;
  }

  getKeyByValue(object, value) {
    return Object.keys(object).find(key => object[key] === value);
  }

  shiftComponentUp(component_id) {
    let shifted_component = this.components.splice(component_id, 1);
    this.components.splice(component_id - 1, 0, shifted_component[0]);
  }

  shiftComponentDown(component_id) {
    let shifted_component = this.components.splice(component_id, 1);
    this.components.splice(component_id + 1, 0, shifted_component[0]);
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

    if (this.compoundUpdateForm.invalid) {
      return;
    }

    if (this.components.length === 0) {
      console.error("Set action or compound");
      return;
    }

    this.compoundCreateRequest['description'] = f.description.value;
    this.compoundCreateRequest['name'] = f.name.value;
    this.compoundCreateRequest['steps'] = this.components.map(item => {
      const step: StepRepresentation = {
        id: item.component.id,
        action: item.isAction,
        parameters: item.parameters
      }
      return step;
    });


    this.compoundService.updateCompound(this.compound_id, this.compoundCreateRequest).subscribe(() => {
        this.router.navigateByUrl('/libraries-menu/compounds').then();
      },
      () => {
        console.error("Error of creation");
      })
  }

  get formControls() {
    return this.compoundUpdateForm.controls;
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

  componentPrepare(component, componentParams?) {
    let step = new Step();

    step.id = this.step_id++;
    step.isAction = component.type !== 'COMPOUND';
    step.component = component;
    step.parameters = new Map<String, String>();

    if (componentParams === undefined) {
      component.parameterNames.map(param => {
        step.parameters[param] = '';
      });
    }
    else {
      step.parameters = componentParams;
    }

    let parentParams = {};
    component.parameterNames.forEach(param => {
      parentParams[param] = "${" + param + "}";
      this.compoundUpdateForm.addControl(step.id + '-' + param, new FormControl('', Validators.required));
    });
    if (component.steps) {
      let mappingParams = {};
      console.log(parentParams)
      this.recursiveStepParsing(component.steps, mappingParams, parentParams);
    }
    this.components.push(step);
    console.log(this.formControls)

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
          }
          else {
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
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}

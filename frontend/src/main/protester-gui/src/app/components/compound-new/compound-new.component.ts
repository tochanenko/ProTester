import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {MatBottomSheet} from "@angular/material/bottom-sheet";
import {BottomSheetComponent} from "../bottom-sheet/bottom-sheet.component";
import {LibraryBottomsheetInteractionService} from "../../services/library/library-bottomsheet-interaction.service";
import {Subscription} from "rxjs";
import {Step} from "../../models/step.model";
import {Router} from "@angular/router";
import {CompoundManageService} from "../../services/compound-manage.service";
import {StepRepresentation} from "../../models/StepRepresentation";

@Component({
  selector: 'app-library-new',
  templateUrl: './compound-new.component.html',
  styleUrls: ['./compound-new.component.css']
})

export class CompoundNewComponent implements OnInit {
  validatorsConfig = {
    name: {
      minLength: 5,
      maxLength: 15
    },
    description: {
      maxLength: 200
    }
  }

  private actionSubscription: Subscription;
  private compoundSubscription: Subscription;
  compoundCreateForm: FormGroup;

  components: Step[] = [];
  step_id: number = 1;
  bottomSheetData = {};
  componentCtx = {
    components: this.components
  }
  url = "";

  componentParamsForm: FormGroup;
  compoundCreateRequest = {};

  constructor(
    private formBuilder: FormBuilder,
    private _bottomSheet: MatBottomSheet,
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
    this.compoundCreateForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(this.validatorsConfig.name.minLength), Validators.maxLength(this.validatorsConfig.name.maxLength)]],
      description: ['', [Validators.required, Validators.maxLength(this.validatorsConfig.description.maxLength)]]
    })
  }

  getComponentsOfSteps(steps) {
    return {array: steps.map(step => step.component)};
  }

  getStepId(step) {
    return step.component.name + '-' + step.id;
  }

  onSubmit(): void {
    const f = this.formControls;


    if (this.compoundCreateForm.invalid) {
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
    console.log(this.compoundCreateRequest);

    this.compoundService.createCompound(this.compoundCreateRequest).subscribe(() => {
        this.router.navigateByUrl('/compound').then();
      },
      () => {
        console.error("Error of creation");
      })
  }

  get formControls() {
    return this.compoundCreateForm.controls;
  }

  updateActionsArray(): void {
    this.actionSubscription = this.interactionService.actionsArrayObserver.subscribe(action => {
      let step = new Step();
      step.id = this.step_id++;
      step.isAction = true;
      step.component = action;
      step.parameters = new Map<String, String>();
      action.parameterNames.map(param => {
        step.parameters[param] = "";
      });
      action.parameterNames.forEach(param => {
        this.compoundCreateForm.addControl(step.id + '-' + param, new FormControl('', Validators.required));
      })
      this.components.push(step);
    });
  }

  updateCompoundsArray(): void {
    this.compoundSubscription = this.interactionService.compoundArrayObserver.subscribe(compound => {
      let step = new Step();
      step.id = this.step_id++;
      step.isAction = false;
      step.component = compound;
      step.parameters = new Map<String, String>();
      compound.parameterNames.map(param => {
        step.parameters[param] = "";
      });

      compound.parameterNames.forEach(param => {
        this.compoundCreateForm.addControl(step.id + '-' + param, new FormControl('', Validators.required));
      })
      this.components.push(step);
    });
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
      data: {components: this.bottomSheetData}
    });
  }

  getAllActionsForBottomSheet(): void {
    this.compoundService.getAllActions().subscribe(data => {
      this.bottomSheetData['actions'] = data['list'];
    });
  }

  getAllCompoundsForBottomSheet(): void {
    this.compoundService.getAllCompounds().subscribe(data => {
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

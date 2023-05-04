import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TestScenarioRoutingModule } from './test-scenario-routing.module';
import { CreateComponent } from "./create/create.component";
import { SearchComponent } from "./search/search.component";
import { ViewComponent } from "./view/view.component";
import { MaterialModule } from "../../../services/material.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { EditComponent } from './edit/edit.component';


@NgModule({
  declarations: [
    CreateComponent,
    SearchComponent,
    ViewComponent,
    EditComponent
  ],
  imports: [
    CommonModule,
    TestScenarioRoutingModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class TestScenarioModule { }

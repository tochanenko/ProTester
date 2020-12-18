import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TestScenarioRoutingModule } from './test-scenario-routing.module';
import { CreateComponent } from "./create/create.component";
import { SearchComponent } from "./search/search.component";
import { ViewComponent } from "./view/view.component";
import { MaterialModule } from "../../../services/material.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [
    CreateComponent,
    SearchComponent,
    ViewComponent
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
